Virtual Basketball
Author: Ben Holzhauer
Date: 12/12/2016
Class: CS 335, Section 1
------------------------
The purpose of this program is to simulate a game of basketball free throw. The player can move left and right using the 'o' and 'p' keys, and can adjust their aim using the 'w/a/s/d' keys. Ready to throw, the player can press the space bar to toss the ball at a desired force level. A force level gauge appears, moving up from 0% to 100% and back down to 0%. If the player does not throw, by pressing the space bar again, the gauge will cycle one more time. If the user does not throw this time, the bar will vanish and they will have to try again.

As the ball moves through the air, it follows a path of projectile motion, determined by the force level and gravity. The ball will bounce back if it hits the backboard, rim, or pole. If the ball goes through the hoop, a winning sound will play, as they made the basket. If the ball misses either by hitting the rim, going out of bounds, or hitting the floor, a losing sound will play.

After each throw, a replay button will appear, allowing for the player to watch the previous throw from a different perspective. If the button is clicked, replay mode will be entered. The perspective is controlled with the mouse, circling around the court with 'x' movement and panning up and down with 'y' movement. The player can also set the replay speed to either 1, 0.25, or 0.1 times the normal speed, using the bracket keys. If anywhere else is clicked when presented with the replay button or the user clicks the screen in replay mode, a new game will start.

The package for this program includes two ".java" files, "InitGL.java" and "JoglEventListener.java". "InitGL.java" sets up the game window and creates the OpenGL rendering canvas. "JoglEventListener.java" consists of the game world and its design, game mechanics, keyboard events, and mouse events.

Also included with the program are nine texture files and two audio files. The texture files are used for the backboard, basketball, skybox, floor, hoop, and replay button. The audio files are for wins and losses in game.