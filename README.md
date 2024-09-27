# OEL remake
## Description
Project trying to rewrite a Commodore 64 game ***OEL Pompowacze*** in java with few improvements

## Downloading without compiling
- Install a Java 17 (JRE or JDK) of your choice. (for instance [Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) or [Liberica](https://bell-sw.com/pages/downloads/#jdk-17-lts))
- Go to the [releases](https://github.com/Zabbum/oel-remake/releases) and download the latest .jar file.
- Now you can simply run the file and play! 
  - If you want to launch game using command, you can do it using `java -jar path/to/file/oel-remake.jar`

## Components

**Application is the main class containing static main() method that you are supposed to run**

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
- Make this whole project more modifiable
- Add option to play via network
- Fix formatting
  - Implement javadoc