- text input commands for UI: (type "help" in the UI to see these again)
  these can be inputted into the "input commands here" field in the UI to see information about the nodes and attacks

	show names
	show connections
	show xypos
	show latlon
	show firewall
	show attacks
	show onlinestatus
	show firewalllog
	update 

- also, you may hover your mouse over a node to see all the info for it in the text box labeled "Node info will show here"

Problems / Solutions:
P: I got a blank screen when running the app.
S: check "backgroundImagePath" in "UI" and make sure it leads to src\resources\map_1810x908.PNG
   check "graphPath"and "attackPath" in "Main" and make sure it leads to src\resources\Graph.txt / Attack.txt
   you will get a blank screen if the program can't find these things. change them to absolute paths if need be

P: I got a blank screen when running the app with a new Attack.txt
S: make sure the new Attack.txt has at least 1 line of text even if its just the city name with no attack
   you will get random errors if Attack.txt is empty

P: I got an error/blank screen with Graph.txt
S: make sure Graph.txt does not have a empty line at the end of the file, this will cause problems

Other notes:
- If running this app on a Mac laptop, the app window size may seem really big for your screen. Unfortunately it is
not resizeable


-----------------------------------------------------------------------------------------------------------------------------------------------


things that need to be done:
- function to calculate distance between nodes so that another function may be able to find the shortest route between node a and b
- make window resizeable for small screen users
