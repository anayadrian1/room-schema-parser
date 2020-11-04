package edu.cnm.deepdive.model;

import com.google.gson.annotations.Expose;
import java.util.stream.Stream;

public class Schema {

  private static final String ENTITY_PLACEHOLDER = "${TABLE_NAME}";
  private static final String VIEW_PLACEHOLDER = "${VIEW_NAME}";

  @Expose
  private Database database;

  public Database getDatabase() {
    return database;
  }

  public void setDatabase(Database database) {
    this.database = database;
  }

  public Stream<String> getDdl() {
    return Stream.concat(
        database.getEntities().stream()
        .flatMap((entity) -> // flatmap for stream: take an item and turn it into a stream; put all the items from all the streams into one single stream
          Stream.concat(
              Stream.of(entity.getDdl()),
              entity.getIndices().stream()
                  .map(Index::getDdl)
          )
          .map((s) -> s.replace(ENTITY_PLACEHOLDER, entity.getName()))
        ),
        database.getViews().stream()
        .map((view) -> view.getDdl().replace(VIEW_PLACEHOLDER, view.getName()))
    );
  }
}
