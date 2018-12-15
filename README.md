# minesweeper-scala-api

This project is intended as an example API that controls access to the game Minesweeper.

In its current version, most of the game functionalities are implemented: a new game can be started, and the board can be changed. When a bomb is clicked, all bombs are revealed and the game is signaled as lost.

However, planting flags and figuring out when the game is won are yet to be implemented.

This API was deployed to `Heroku` and is available for pinging in the URL `http://whispering-atoll-52291.herokuapp.com`

The API has the following Routes:

**Routes**
---

*start*
----
  Route that is called to start the game. It does not take any parameters. The returned values should be the board dimensions, number of bombs, an encoded version of the box and the current game status.

* **URL**

  `/start`

* **Method:**

  `GET`

* **Success Response:**

  When the call is successful, we will have the following Response:

  * **Code:** 200 <br />
    **Content:**

    `{ id: UUID, rows: Int, cols: Int, bombs: Int, status: String, cells: Array[{contet: String, seen: boolan}]  }`

    Breaking down the response we have:

    - `id:` the ID of the game being played
    - `rows:` the number of rows in the boards
    - `cols:` the number of columns in the board
    - `bombs:` how many bombs were placed in board
    - `status`: current game status. In current implementation may be either `ongoing` or `lost`
    - `cells:` an array with information pertaining to the cells of the boardgame. Each cell has the cell content in `content` and whether or not it has been clicked on in `seen`

* **Sample Call:**

  ` curl https://whispering-atoll-52291.herokuapp.com/start`

  *move*
  ----------
  Method that exposes the move functionality. It allows an user to actually play the game, by passing in the request body which row and column they wish to reveal, and in which board, and they receive a response similar to the */start* route.

* **URL**

  `/move`

* **Method:**

   `POST`

* **Data Params**

They request body is expected to have the following format:

  `{row: Int, col: Int, board_id: UUID}`

  where `row` and `col`, are, respectively, the row and column that the player wishes to explore. `board_id` represents the id of the boardgame.

* **Success Response:**

When the call is successful, we will have the following Response:

* **Code:** 200 <br />
  **Content:**

  `{ id: UUID, rows: Int, cols: Int, bombs: Int, status: String, cells: Array[{contet: String, seen: boolan}]  }`

  Breaking down the response we have:

  - `id:` the ID of the game being played
  - `rows:` the number of rows in the boards
  - `cols:` the number of columns in the board
  - `bombs:` how many bombs were placed in board
  - `status`: current game status. In current implementation may be either `ongoing` or `lost`
  - `cells:` an array with information pertaining to the cells of the boardgame. Each cell has the cell content in `content` and whether or not it has been clicked on in `seen`

* **Sample Call:**

  `curl -d '{"row":0, "col":2, "board_id":"41ea2545-26f7-4ad9-8002-5046f4d12602" }' -H "Content-Type: application/json" -X POST https://whispering-atoll-52291.herokuapp.com/move`

  ___

  **Design Decisions**
  ---

  To keep the state of the board and guarantee that the client in the front end would not need to keep information about the board, I decided to store all board information on `PostgreSQL`.

  The schema used was as follows:

  ```sql
  CREATE SCHEMA IF NOT EXISTS minesweeper;

  CREATE TABLE minesweeper.boardstate (
    id uuid PRIMARY KEY, -- board Id
    rows integer NOT NULL, -- number of rows
    cols integer NOT NULL, -- number of columns
    encoded_board text NOT NULL, -- board encoded as a string with rows*cols chars
    current_board text NOT NULL, -- vision player currently has of the board
    bombs integer NOT NULL, -- number of bombs placed in board
    game_status text NOT NULL -- game status - currently either 'ongoing' or 'lost'
  );
  ```

  When starting a new game, a random board is created, its cells are encoded into a string and that representation, along with its properties, are inserted in the above table.

  A move will first read from the table to recover the suitable board state (this is necessary since RESTful APIs should not maintain state), then, after the move is processed, the table is updated to reflect the changes done to the board. This double database communication might lead to a slight delay when clicking cells during game.

  ---

  **Front End**
  ---

  A sample front end was developed to communicate with this API and allow for the game to be played and can be found in [here](http://fathomless-river-62164.herokuapp.com/).

  The code containing the front end can be found in [this repo.](https://github.com/gnsantos/minesweeper-front)
