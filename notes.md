# Development notes on original source code

## Lines in source code
- 8797 - getting amount of players
- 9670 - generating oil prizes
- 10524 - showing expected oil prize trends
- 11131 - choosing an action
- 11215 - game finish
- 15183 - sabotage
- 11159 - round finish

## Code to understandable speech
- `GOSUB21104` - wait
- `GOSUB21006` - press any key
- `POKEl,15` - ?
- `POKE 53280,?`, `POKE 53281,?` - change colors
- `z(x)` = `INT(RND(1)*x)+1`

## Variable names
- `t`, `k` - iterations
- `sz` - amount of players
- `bk()` - debts
- `bf()` - oilfields names
- `bp()` - oilfields prices
- `fm()` - oil amount in oilfields
- `f()` - oilfields' abilities to pump oil
- `bt()` - oilfields' required depth to unlock ability to pump oil
- `ra()` - oilfields' old owners
- `fb()` - oilfields' ownerships
- `pa()` - oilfields' amount of pumps
- `gf()` - oilfields' amount of oil to sell
- `ta()` - oilfields' amount of cars
- `ab()` - oilfields' current digging depth
- `bm()` - oilfields' remaining drill buffers
- `bb()` - oilfields' available oil amount
- `gg()` - oilfields' amount of oil pumped out
- `pf$()` - pump productions names
- `pf()` - pump productions' industry prices
- `pw()` - pump productions' amounts of products
- `p()` - pump productions' product prices
- `pp()` - pump productions' ownerships
- `b()` - drill productions
- `bw()` - drill productions' amounts of products
- `bg()` - drill productions' industry prices
- `tg()` - cars productions
- `tw()` - cars productions' amounts of products
- `tp()` - cars productions' industry prices
- `tb()` - cars productions' ownerships
- `l` - 54296 (kinda pointer)
- `a` - 54277 (kinda pointer)
- `si` - 54272
- `rp()` - oil prizes
- `bd()` - price of drill from productions
- `bw()` - amount of available drills in production
- `bb()` - drill production ownership
- `ks()` - players' balance
- `r` - current round
- `z2` - player's choice
- `ks()` - players' balance