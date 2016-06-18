CREATE TABLE jobs (
  id         BIGINT       NOT NULL,
  algo       VARCHAR(255) NOT NULL,
  endDate    TIMESTAMP,
  srcUrl     VARCHAR(255) NOT NULL,
  stackTrace VARCHAR(255),
  startDate  TIMESTAMP,
  status     VARCHAR(255),
  uuid       VARCHAR(255),
  PRIMARY KEY (id)
);