# Chess-A.I
### **Inspiration**  
I've always been fascinated by chess and the challenge of creating an AI opponent that can play strategically. This project was an opportunity to combine my passion for programming with my interest in game development. I wanted to build a Chess AI that could consistently checkmate the player, testing my ability to implement both **object-oriented design** and **AI decision-making** in Java. While the current version of the AI only makes random moves, my goal is to optimize it over time to play intelligently and competitively.  

### **What it does**  
Chess AI is a **Java-based chess game** with an interactive **graphical user interface (GUI)** that allows players to compete against a bot. The game features **legal move validation, turn-based logic, and piece interaction**, ensuring smooth and rule-compliant gameplay. While the AI is not fully optimized yet, the bot currently makes **random moves**, laying the foundation for future improvements. The project aims to eventually include **strategic move selection** using AI algorithms like **minimax with alpha-beta pruning**.  

### **How we built it**  
The project was built using **Java, Swing, and AWT** for the GUI, while **JUnit** was used for testing and validating game logic. The **object-oriented design** follows a structured approach where all chess pieces inherit from a **base class**, allowing for modular and reusable code. Each piece follows standard chess rules, with move validation ensuring fair play. The game also includes **event handling** for player interactions, making the board responsive to clicks and move selections.  

### **Challenges I ran into**  
One of the biggest challenges was implementing **accurate move validation**, especially for complex rules like **castling, en passant, and check detection**. Ensuring that the game followed the correct turn order while updating the GUI in real-time also required careful event handling. Another challenge was the **AI logic**, as designing an opponent that plays intelligent moves is significantly more complex than simply enforcing game rules. While the bot currently makes random moves, optimizing its decision-making remains a key challenge.  

### **Accomplishments that I'm proud of**  
I'm proud of successfully implementing a **fully functional chess game** with a structured foundation for AI integration. The use of **inheritance and object-oriented principles** made the code efficient and maintainable. Additionally, I gained hands-on experience with **GUI programming in Java**, making the game interactive and user-friendly. Writing **unit tests with JUnit** was another accomplishment, as it helped validate game mechanics and ensure robustness.  

### **What I Learned**  
This project deepened my understanding of **object-oriented programming**, particularly in leveraging **inheritance and polymorphism** to manage chess pieces efficiently. I also improved my skills in **GUI development** using Java Swing and learned how to handle **event-driven programming**. Implementing move validation taught me about **algorithmic problem-solving**, and working with JUnit reinforced the importance of **testing and debugging**. Looking ahead, I plan to explore **AI algorithms like minimax and alpha-beta pruning** to enhance the Chess AI’s decision-making.  

### **What’s Next for Chess AI**  
The next major step for Chess AI is implementing **an intelligent move-selection algorithm** to replace the current random move generator. My primary focus will be on **minimax with alpha-beta pruning**, a strategy commonly used in chess engines to evaluate and predict the best possible moves. Additionally, I plan to integrate **move scoring heuristics**, such as prioritizing center control, piece safety, and strategic positioning.  

Beyond AI improvements, I also want to enhance the **user experience** by refining the GUI, adding **game history tracking**, and possibly introducing **different difficulty levels** for the bot. Another potential feature is **multiplayer support**, allowing two players to compete against each other instead of just playing against the AI. Long-term, I would love to explore **machine learning techniques** to train the bot dynamically based on past games, making it more adaptive and challenging over time.
<br>
![ChessAIVideo (1)](https://github.com/user-attachments/assets/8190b29d-df9b-436f-8f0d-d7ef648d09ee)
