# EPIC BATTLE RPG

É EPIC BATTLE RPG uma API para um jogo RPG estilo Dungeons & Dragon.

## Principais endpoints:

### Characters

`GET`       /api/characters<br>
`GET`       /api/characters/{characterId}<br>
`POST`      /api/characters<br>
`PUT`       /api/characters<br>
`DELETE`    /api/characters/{characterID}<br>

### Game

`GET`       /api/game<br>
`GET`       /api/game/{gameId}<br>
`GET`       /api/game/initiative<br>
`GET`       /api/game/turns<br>
`GET`       /api/game/moves<br>
`GET`       /api/game/whoplaysnow<br>
`GET`       /api/game/initiative/winner<br>
`POST`      /api/game/initiative<br>
`POST`      /api/game/{gameId}/attack<br>
`POST`      /api/game/{gameId}/defense<br>
`POST`      /api/game/{gameId}/applydamage<br>
