CREATE SCHEMA IF NOT EXISTS minesweeper;

CREATE TABLE minesweeper.boardstate (
  id uuid PRIMARY KEY,
  rows integer NOT NULL,
  cols integer NOT NULL,
  encoded_board text NOT NULL,
  current_board text NOT NULL,
  bombs integer NOT NULL,
  game_status text NOT NULL
);
