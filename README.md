# Test to use Github with Eclipse!

Su: 

https://www.geeksforgeeks.org/how-to-use-git-with-eclipse/ 

https://www.geeksforgeeks.org/how-to-export-eclipse-projects-to-github/#:~:text=Step%201%3A%20Open%20Eclipse%20IDE,go%20to%20Team%2D%3Ecommit. 

ho trovato la guida che spiega come fare a creare su git hub un progetto iniziato su eclipse e viceversa.


____


## How to Export Eclipse projects to GitHub?


Step 1: Open Eclipse IDE and right-click on the project you want to push and go to Team->share project.

Step 2: It will add the project to the given repository

Step 3: Again right-click on the project and go to Team->commit.

Step 4: Drag and Drop the files you want to commit from Unchanged Changes to Staged Changes.

Step 5: Write the commit message in “Commit Message” and click “Commit and Push“.

!important

Step 6: Fill in your UserID and password of GitHub and click “Log in“. **

Step 7: Push to the new-branch of GitHub Repository and click “Push“.


_____

**

Ho riscontrato il seguente problema durante il login al mio account Git Hub, che fortunatamente sono riuscita a risolvere grazie a un utente su stackoverflow:

(https://stackoverflow.com/questions/14259265/egit-not-authorized#:~:text=If%20you're%20using%20Two,this%20instead%20of%20a%20password)

La soluzione è la seguente:

If you're using Two Factor Authentication on GitHub, the "not authorized" error can be returned even if you are using the correct username and password. This can be resolved by generating a personal access token. After generating the secure access token,### we'll use this instead of a password.
