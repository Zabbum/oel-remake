# OEL remake
## Description
Project trying to rewrite a Commodore 64 game ***OEL Pompowacze*** in java with few improvements

## Components
#### AppLanterna is the main class that you are supposed to run
- `target/` cantains java classes
- `lib/` contains all the libraries
- `src/` contains all the source code of remake
- `vice-snapshots/` - contains VICE64 C64 emulator snapshots from original game
    - `vice-snapshot-start.vsf` is a game start
    - `vice-snapshot-start1.vsf` is a game start (2 players, A and Bs)
    - `vice-snapshot-drills.vsf` is a drill buying menu
    - `vice-snapshot-cars.vsf` is a car buying menu
    - `vice-snapshot-pumps.vsf` is a pump buying menu
    - `vice-snapshot-sabotage.vsf` is a sabotage menu
- `oel_pompowacze.txt` is original game sourcecode
- `notes.md` are development notes on original sourcecode

## Current roadmap
- Implement ASCII arts
- Extract all the text to a languagepack
- Make this whole project more modifiable
- Add option to play via network