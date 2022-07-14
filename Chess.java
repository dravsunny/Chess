package chess;
//Jaideep Duggempudi
//Suneet Dravida
import java.util.ArrayList;
import java.util.HashMap;
/**
 * The Chess class is the main controller class that initiates, sustains, and ends the
 * game when checkmate is reached.
 *
 * @author Jaideep Duggempudi
 * @author Suneet Dravida
 *
 */

public class Chess{
    /**
     * Lambda function that increments x and returns the new value of x
     */
    public QRS add1 = (x) -> {x = x + 1; return x;};
    /**
     * Lambda function that decrements x and returns the new value of x
     */
    public QRS minus1 = (x) -> {x = x - 1; return x;};
    /**
     * Lambda function that makes no changes to x and returns x
     */
    public QRS nochange = (x) -> {return x;};

    /**
     * Integer variable that stores 1
     */
    public final int wP = 1;//whites pawns move in the positive y direction moving either one space or 2 spaces
    /**
     * Integer variable that stores 1
     */
    public final int bP = -1;//black pawns move in the negative y direction moving either one space or 2 spaces
    /**
     * HashMap that stores where each piece is (key, value) -> eg (e2, ChessPiece)
     */
    public HashMap<String, ChessPiece> chessboard = new HashMap<String, ChessPiece>();//hashmap that stores the chessboard
    /**
     * ArrayList that stores all the possible moves a certain piece is able to make
     */
    public ArrayList<String> vm;//stores all possible valid moves that a certain piece can make
    /**
     * ArrayList that stores all the possible moves a player can make if their king is in check
     */
    public ArrayList<String> checkmoves;//stores all possible moves a player can make when their king is in check
    /**
     * Character that stores the current players side (white or black)
     */
    public char cp = 'w';//who is the player as of right now
    /**
     * Character that stores the enemy players side (white or black)
     */
    public char op = 'b';//who is the opponent as of right now
    /**
     * String value that keeps track of where the white king is at all times
     */
    public String wK = "e1";//stores where the white king is after every move so we can see if the king is in check form that position
    /**
     * String value that keeps track of where the black king is at all times
     */
    public String bK = "e8";//stores where the black king is after every move so we can see if the king is in check form that position
    /**
     * String value that keeps track of the chess piece that was previously moved
     */
    public String ep;//Since enpassant is only possible after a pawns first move, ep keeps track of the previous piece to see if enpassant was performed otherwise the enpassant indicator is set to false for the previous piece
    /**
     * String value that stores the current move eg e2 e4
     */
    public String currentmove;//stores current move
    /**
     * Boolean value that is true if a king is in check and false if it is not
     */
    public boolean check;// true if king is in check
    /**
     * Arraylist that stores every move player makes
     */
    public ArrayList<String> results = new ArrayList<String>();
    /**
     * The start method is the main controller method of the Chess class, it prompts each player for input (move),
     * then displays each move on on the chessboard that by printing to the console after every move. Every time a
     * player makes a move, start calls the check method to see if the enemy king is in check, if so, the checkmate
     * method is called to see if there are any moves possible to remove the king from check, if not checkmate otherwise
     * player must make a move to get their king in check. If there is no check, start calls the ismoveinvalid to see
     * if the move the player inputed is legal.
     */
    public void start()
    {
        while(true)
        {
            String[] fromto;//string array that stores the starting square and destination square, fromto[2] can also stores the promotional piece

            cp = 'w';//current player is now white
            op = 'b';//opponent player is now black

            if(check(wK))//determines if the white king is in check after black makes a move
            {
                checkmate(wK);//checks for valid moves possible when white in check, if none checkmate
                if(checkmoves.isEmpty())
                {
                   for(String i: results)
                   {
                       System.out.println(i);
                   }
                   System.out.println("Checkmate" + "\n" + "Black wins");
                   break;
                }
                System.out.println("Check");
                System.out.print("White's Move: ");
                currentmove = StdIn.readLine();//stores what the player moved

                if(currentmove.contains("resign")) { //if white wants to resign, game prints out "Black wins" and game ends
                    for(String i: results)
                    {
                        System.out.println(i);
                    }
                    System.out.println("resign");
                    System.out.println("Black wins");
                	break;
                }

                if(currentmove.contains("draw?")) { //if current player wants to draw, in the opponent's next move, they must accept
                    results.add(currentmove);
                    System.out.print("Black's move: ");
                    String draw = StdIn.readLine();
                	results.add(draw);
                	if(draw.equals("draw")){
                        for(String i: results)
                        {
                            System.out.println(i);
                        }
                		break;
                	}
                }

                while(!checkmoves.contains(currentmove.substring(0, 5)))//sees if what player input is valid move when checked, if not reprompts the player to put in valid move
                {
                    System.out.println("Illegal move, try again");//if player puts in a move that keeps their king in check illegal move is printed
                    System.out.print("White's's Move: ");
                    currentmove = StdIn.readLine();
                }
                results.add(currentmove);
                fromto = currentmove.split(" ");
                if(chessboard.get(fromto[0]).getpiece().equals("wK"))//updates the kings position if king was moved to escape check
                {
                    wK = fromto[1];
                }
                if(chessboard.get(fromto[0]).getpiece().charAt(1) == 'R' || chessboard.get(fromto[0]).getpiece().charAt(1) == 'K')//updates the castlecounter if king or rook was moved in order to escape check, used for castling purposes
                {
                    chessboard.get(fromto[0]).decrementcastleCounter();
                }
                performMove(fromto[0], fromto[1]);//if move is valid, move is performed
                if(ep != null && chessboard.get(ep) != null)//ep stores the piece previously moved, if the move did not take part in enpassant this move, enpasant indicator becomes false
                {
                    chessboard.get(ep).enPassantStatus(false);
                }
                ep = fromto[1];//the currently moved now becomes the previously moved piece
                display();//chessboard is displayed

            }
            else//when white isn't in check
            {
                System.out.print("White's Move: ");
                currentmove = StdIn.readLine();

                if(currentmove.contains("resign")) { //if white wants to resign, game prints out "Black wins" and game ends
                    for(String i: results)
                    {
                        System.out.println(i);
                    }
                    System.out.println("resign");
                    System.out.println("Black wins");
                	break;
                }

                if(currentmove.contains("draw?")) { //if current player wants to draw, in the opponent's next move, they must accept
                    results.add(currentmove);
                    System.out.print("Black's move: ");
                    String draw = StdIn.readLine();
                	results.add(draw);
                	if(draw.equals("draw")) {
                        for(String i: results)
                        {
                            System.out.println(i);
                        }
                		break;
                	}
                }
                fromto = currentmove.split(" ");
                while(ismoveinvalid(fromto))//determined if move that player inputed is a legal move
                {
                    System.out.println("Illegal move, try again");// Illegal move, try again is printed to console if player's move was invalid
                    System.out.print("White's Move: ");
                    currentmove = StdIn.readLine();
                    fromto = currentmove.split(" ");
                }
                results.add(currentmove);
                if(ep != null && chessboard.get(ep) != null)
                {
                    chessboard.get(ep).enPassantStatus(false);
                }
                ep = fromto[1];
                display();

            }

            cp = 'b';//changes current player to black
            op = 'w';//changes current player to white

            if(check(bK))//same process as when white is in check
            {

                checkmate(bK);
                if(checkmoves.isEmpty())
                {
                    for(String i: results)
                   {
                       System.out.println(i);
                   }
                   System.out.println("Checkmate" + "\n" + "White wins");
                   break;
                }
                System.out.println("Check");
                System.out.print("Blacks's move: ");
                currentmove = StdIn.readLine();

                if(currentmove.contains("resign")) { //if white wants to resign, game prints out "Black wins" and game ends
                    for(String i: results)
                    {
                        System.out.println(i);
                    }
                    System.out.println("resign");
                    System.out.println("White wins");
                    break;
                }
                if(currentmove.contains("draw?")) { //if current player wants to draw, in the opponent's next move, they must accept
                    results.add(currentmove);
                    System.out.print("Whites's move: ");
                    String draw = StdIn.readLine();
                	results.add(draw);
                	if(draw.equals("draw")) {
                        for(String i: results)
                        {
                            System.out.println(i);
                        }
                		break;
                    }
                }

                while(!checkmoves.contains(currentmove.substring(0, 5)))
                {
                    System.out.println("Illegal move, try again");
                    System.out.print("Blacks's Move: ");
                    currentmove = StdIn.readLine();
                }
                results.add(currentmove);
                fromto = currentmove.split(" ");

                if(chessboard.get(fromto[0]).getpiece().equals("bK"))
                {
                    bK = fromto[1];
                }
                if(chessboard.get(fromto[0]).getpiece().charAt(1) == 'R' || chessboard.get(fromto[0]).getpiece().charAt(1) == 'K')//castling purposes
                {
                    chessboard.get(fromto[0]).decrementcastleCounter();
                }
                performMove(fromto[0], fromto[1]);
                if(ep != null && chessboard.get(ep) != null)
                {
                    chessboard.get(ep).enPassantStatus(false);
                }
                ep = fromto[1];
                display();

            }
            else//determines if what black player inputed was a legal move same process a white
            {
                System.out.print("Black's Move: ");
                currentmove = StdIn.readLine();

                if(currentmove.contains("resign")) { //if white wants to resign, game prints out "Black wins" and game ends
                    for(String i: results)
                    {
                        System.out.println(i);
                    }
                    System.out.println("resign");
                    System.out.println("White wins");
                    break;
                }
                if(currentmove.contains("draw?")) { //if current player wants to draw, in the opponent's next move, they must accept
                    results.add(currentmove);
                    System.out.print("Whites's move: ");
                    String draw = StdIn.readLine();
                	results.add(draw);
                	if(draw.equals("draw")) {
                        for(String i: results)
                        {
                            System.out.println(i);
                        }
                		break;
                	}
                }

                fromto = currentmove.split(" ");
                while(ismoveinvalid(fromto))
                {
                    System.out.println("Illegal move, try again");
                    System.out.print("Black's Move: ");
                    currentmove = StdIn.readLine();
                    fromto = currentmove.split(" ");
                }
                results.add(currentmove);
                if(ep != null && chessboard.get(ep) != null)
                {
                    chessboard.get(ep).enPassantStatus(false);
                }
                ep = fromto[1];
                display();
            }
        }

    }

    /**
     * The ismovevalid method determines if a player (when they are not in check) is a valid move or not.
     * It performs the move if it was deemed legal or returns true prompting the player to input a new move.
     * Castling is also done in this method if it is legal. In laymen's terms, each piece has its own private
     * method that populates vm arraylist of all the possible moves that are possible from it's starting
     * position. If the destination that the player inputed is found in vm it is a legal move.
     *
     * @param fromto string array that stores the starting square and destination square, fromto[2] can also stores the promotional piece
     * @return false if move was legal, returns true if move was not legal
     */

    public boolean ismoveinvalid(String[] fromto)
    {

        String from = fromto[0];//stores where player wants to move from
        String to = fromto[1];//stores where player wants to go

        vm = new ArrayList<String>();//creates a new arrayllist instance, vm stores all the possible moves the piece player wants to move can make
        checkmoves = new ArrayList<String>();

        if(chessboard.get(from) == null)//if player select an empty square not allowed
        {
            return true;
        }
        else if(chessboard.get(from).getpiece().charAt(0) == op)//a players tries to move other players piece not allowed
        {
            return true;
        }
        else if(chessboard.get(to) != null && chessboard.get(to).getpiece().charAt(0) == cp)//a player tries to take their own piece not allowed
        {
            return true;
        }
        else if(chessboard.get(to) != null && chessboard.get(to).getpiece().charAt(1) == 'K')//a player tries to take a king not allowed
        {
            return true;
        }
        else if(chessboard.get(from).getpiece().charAt(1) == 'N')//if players chooses to move their knight
        {
            knight(from);

        }
        else if(chessboard.get(from).getpiece().charAt(1) == 'B')//if players chooses to move their bishop
        {
            bishop(from);
        }
        else if(chessboard.get(from).getpiece().charAt(1) == 'R')//if players chooses to move their rook
        {
            rook(from);

        }
        else if(chessboard.get(from).getpiece().charAt(1) == 'Q')//if players chooses to move their queen
        {
            queen(from);
        }
        else if(chessboard.get(from).getpiece().equals("wP"))//if players chooses to move their white pawn
        {
            pawn(from, wP);

        }
        else if (chessboard.get(from).getpiece().equals("bP"))//if player chooses to move their black pawn
        {
            pawn(from, bP);

        }
        else if (chessboard.get(from).getpiece().equals("wK"))//player chooses to move whiteking
        {
            //castling method
            if(from.equals("e1") && to.equals("g1") && chessboard.get(from).getcastleCounter() == 1)//determines if player wants to kings side castle and checks if king was moved, not possible to castle then
            {
                if(chessboard.get("f1") == null && !check("f1") && chessboard.get("g1") == null && !check("g1"))//determines if space between rook and king is empty and checks to see if spaces that king must cross over are being threatened by an enemy piece
                {
                    if(chessboard.get("h1") != null && chessboard.get("h1").getpiece().equals("wR") && chessboard.get("h1").getcastleCounter() == 1)//determines whether the rook was moved, not possible to castle then, 1 would mean pirce was not moved
                    {
                        chessboard.put("f1", chessboard.get("h1"));//rooks castling moves
                        chessboard.put(to, chessboard.get(from));//kings castling move
                        chessboard.remove(from);
                        chessboard.remove("h1");
                        wK = to;//updates white kings positions
                        return false;
                    }
                    return true;

                }
            }
            if(from.equals("e1") && to.equals("c1") && chessboard.get(from).getcastleCounter() == 1)//queens side castle
            {
                if(chessboard.get("d1") == null && !check("d1") && chessboard.get("c1") == null && !check("c1") && chessboard.get("b1") == null)
                {
                    if(chessboard.get("a1") != null && chessboard.get("a1").getpiece().equals("wR") && chessboard.get("a1").getcastleCounter() == 1)
                    {
                        chessboard.put("d1", chessboard.get("a1"));//rooks castling moves
                        chessboard.put(to, chessboard.get(from));//kings castling move
                        chessboard.remove(from);
                        chessboard.remove("a1");
                        wK = to;//updates white kings position
                        return false;
                    }
                    return true;
                }
            }
            king(from);//king method is called if player did not want to do castling move with king

        }
        else
        {
            //castling method for black king same process as for white king
            if(from.equals("e8") && to.equals("g8") && chessboard.get(from).getcastleCounter() == 1)//kings side castle
            {
                if(chessboard.get("f8") == null && !check("f8") && chessboard.get("g8") == null && !check("g8"))
                {
                    if(chessboard.get("h8") != null && chessboard.get("h8").getpiece().equals("bR") && chessboard.get("h8").getcastleCounter() == 1)
                    {
                        chessboard.put("f8", chessboard.get("h8"));//rooks castling moves
                        chessboard.put(to, chessboard.get(from));//kings castling move
                        chessboard.remove(from);
                        chessboard.remove("h8");
                        bK = to;
                        return false;
                    }
                    return true;
                }
            }
            if(from.equals("e8") && to.equals("c8") && chessboard.get(from).getcastleCounter() == 1)//queens side castle
            {
                if(chessboard.get("d8") == null && !check("d8") && chessboard.get("c8") == null && !check("c8") && chessboard.get("b8") == null)
                {
                    if(chessboard.get("a8") != null && chessboard.get("a8").getpiece().equals("bR") && chessboard.get("a8").getcastleCounter() == 1)
                    {
                        chessboard.put("d8", chessboard.get("a8"));//rooks castling moves
                        chessboard.put(to, chessboard.get(from));//kings castling move
                        chessboard.remove(from);
                        chessboard.remove("a8");
                        bK = to;
                        return false;
                    }
                    return true;
                }
            }
            king(from);
        }

        if(chessboard.get(from).getpiece().charAt(1) == 'K')//if piece was a king
        {
            if(chessboard.get(from).getpiece().equals("wK"))
            {
                if(vm.contains(to))//sees if where player wants to move is valid
                {
                    chessboard.get(from).decrementcastleCounter();//castle counter is decrement no longer able to castle with white king
                    performMove(from, to);
                    wK = to;//updates white kings position
                    return false;
                }

            }
            else//for black king
            {
                if(vm.contains(to))
                {
                    chessboard.get(from).decrementcastleCounter();//castle counter is decremented not longer able to castle with black king
                    performMove(from, to);//sees if where player wants to move is valid
                    bK = to;//updates black kings position
                    return false;
                }

            }
            return true;
        }
        else//if piece was any piece besides king
        {
            if(vm.contains(to))
            {
                if(cp == 'w' && simulate(from, to, wK))//this is to prevent moving your own piece that is protecting your king form check - simulate returns true if king is checked
                {
                    return true;

                }
                if(cp == 'b' && simulate(from, to, bK))
                {
                    return true;
                }
                if(chessboard.get(from).getpiece().charAt(1) == 'R')//decrements castlecounter of rook, no longer possible to castle on this rooks side
                {
                    chessboard.get(from).getcastleCounter();
                }
                performMove(from, to);
                return false;
            }

        }

        return true;



    }
   /**
    * The recursive_check method is used to see if the king is being threatened by the enemy rook, bishop, or queen. It
    * can also test if a empty square is begin threatened by an enemy bishop, rook, or queen or if a enemy piece is being
    * protected by an enemy bishop, rook, or queen preventing the king from moving to that square or capturing the enemy
    * piece respectively. Two lambda functions are used to decrement, increment or not change the x and y position in order to recursively update
    * the square upon which you want to test for enemy pieces.
    *
    * @param m Lambda function that can increment, decrement, or do nothing to the x grid position of piece
    * @param n Lambda function that can increment, decrement, or do nothing to the y grid position of piece
    * @param x The x position of the piece eq a = 1 b = 2
    * @param y The y position of the piece
    * @param piece Stores what piece you want to test to see is checking the king, protecting a enemy piece, or threatening a empty square
    */
    public void recursive_check(QRS m, QRS n, int x, int y, String piece)//checks to see if king is in check by a rook, queen or bishop
    {//also used to check if a piece cannot be taken by king because it is protected by it's teams bishop, rook, or queen or if a empty square is being threatened preventing king from going there
        if(!checkBounds(x, y))//checks to see if the square is a valid square on the chess board eg returns if g9
        {
            return;
        }
        if(chessboard.get(xytochess(x, y)) == null)//the square is empty
        {
            recursive_check(m, n, m.QRSvalid(x), n.QRSvalid(y), piece);//recursively call function again with new x and y
        }
        else
        {
            if(chessboard.get(xytochess(x, y)) != null && (chessboard.get(xytochess(x, y)).getpiece().equals(op+piece) ||
               chessboard.get(xytochess(x, y)).getpiece().equals(op+"Q")))//checks to see if there is an enemy bishop, rook, or queen checking the king, protecting an enemy piece, or threatening a square
            {
                check = true;//if there is check becomes true
                return;

            }
            if(chessboard.get(xytochess(x, y)).getpiece().charAt(0) == cp)//if square contains own players piece, automatically returns
            {
                return;
            }
        }



    }
    /**
     * The check method has three exclusive functions depending on the situation: 1- It can check to see if a king is in check, 2- it can see if a enemy piece is
     * being protected by another enemy piece, 3- it can see if an empty square is being threatened by an enemy piece. 2 and 3 are used to see if a king can
     * capture an empty piece since its its not possible for the king to capture a protected enemy piece or move to an empty square since it is not possible for
     * the king to move to a threatened square respectively.
     * <p>
     * * indicative of position
     * </p>
     * <p>
     * 1. |   |bP| -> check
     * </p>
     * <p>
     *    |wK*|  |
     * </p>
     * <p>
     * 2. |  |bN*|  |bR| -> protected
     * </p>
     * <p>
     *    |  |wK |  |  |
     * </p>
     * <p>
     * 3. |  | *|  |bR| -> threatened
     * </p>
     * <p>
     *    |  |wK|  |  |
     * </p>
     * @param position can hold kings position to see if it is in check, can hold a non-king chesspiece position, or an empty squares position
     * @return true if the king is in check an position holds the king position, true if an enemy piece is being protected by another enemy piece and position is holding a enemy piece's position, or true if an empty square is being threatened and position holds an empty square position
     */
    public boolean check(String position)//used to see if a king is checked by knight rook queen bishop or pawn
    {//also can be used to see if king can't take opposing piece because it is protected by those pieces
        check = false;

        int x = getx(position);//x position
        int y = gety(position);//y position
        // checks if enemy king is protecting a piece, or threatening a empty square
        if(chessboard.get(xytochess(x+1, y+1)) != null && chessboard.get(xytochess(x+1, y+1)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x+1, y-1)) != null && chessboard.get(xytochess(x+1, y-1)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x+1, y)) != null && chessboard.get(xytochess(x+1, y)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x, y-1)) != null && chessboard.get(xytochess(x, y-1)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x, y+1)) != null && chessboard.get(xytochess(x, y+1)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-1, y-1)) != null && chessboard.get(xytochess(x-1, y-1)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-1, y)) != null && chessboard.get(xytochess(x-1, y)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-1, y+1)) != null && chessboard.get(xytochess(x-1, y+1)).getpiece().equals(op + "K"))
        {
            check = true;
        }
        // checks if any knights are checking king, protecting a piece, or threatenign a empty square
        if(chessboard.get(xytochess(x+1, y+2)) != null && chessboard.get(xytochess(x+1, y+2)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x+1, y-2)) != null && chessboard.get(xytochess(x+1, y-2)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-1, y+2)) != null && chessboard.get(xytochess(x-1, y+2)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-1, y-2)) != null && chessboard.get(xytochess(x-1, y-2)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x+2, y+1)) != null && chessboard.get(xytochess(x+2, y+1)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x+2, y-1)) != null && chessboard.get(xytochess(x+2, y-1)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-2, y+1)) != null && chessboard.get(xytochess(x-2, y+1)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-2, y-1)) != null && chessboard.get(xytochess(x-2, y-1)).getpiece().equals(op + "N"))
        {
            check = true;
        }
        //checks to see if enemy pawn is either checking king or protecting enemy piece or threatening an empty square
        if(chessboard.get(position) != null && chessboard.get(position).getpiece().charAt(0) == 'w')
        {
            pawncheck(position, wP);
        }
        if(chessboard.get(position) != null && chessboard.get(position).getpiece().charAt(0) == 'b')
        {
            pawncheck(position, bP);
        }
        //checks if king threatened by bishop or queen or a enemy piece protected by its own bishop and queen, or empty square being threatened by enemy bishop or queen
        recursive_check(add1, add1, x+1, y+1, "B");
        recursive_check(add1, minus1, x+1, y-1, "B");
        recursive_check(minus1, minus1, x-1, y-1, "B");
        recursive_check(minus1, add1, x-1, y+1, "B");
        //checks if king threatened by rook or queen or a enemy piece protected by its own rook and queen, or empty square being threatened by enemy rook or queen
        recursive_check(add1, nochange, x+1, y, "R");
        recursive_check(nochange, minus1, x, y-1, "R");
        recursive_check(minus1, nochange, x-1, y, "R");
        recursive_check(nochange, add1, x, y+1, "R");


        return check;


    }
    /**
     * The pawncheck method is a special method exclusive to pawns and helper method for the check method. It tests to see if a king is being checked by an enemy
     * pawn if position is a kings position, it can also see if an enemy piece is being protected by an enemy pawn if the position is
     * a position of an enemy piece, or if position is an empty square it can see if that square is begin threatened by an enemy pawn.
     *
     * @param position can hold kings position to see if it is in check by enemy pawn, can hold a non-king chesspiece position, or an empty squares position
     * @param color for white pawns color is +1 since they can only move positive direction, for black pawns color is -1 since they move in the negative y direction
     */
    private void pawncheck(String position, int color)//sees if pawn is checking king or protecting ones of its own pieces preventing opposing king from taking piece or if enemy pawn is threatenign square preventing king from going there
    {
        int x = getx(position);
        int y = gety(position);
        //checks the the corner squares for enemy pawn (y+1) for black pawns and (y-1) for white pawns
        // |  |bP| y = 2
        // |wK|  | y = 1
        if(chessboard.get(xytochess(x+1, y+color)) != null && chessboard.get(xytochess(x+1, y+color)).getpiece().equals(op + "P"))
        {
            check = true;
        }
        if(chessboard.get(xytochess(x-1, y+color)) != null && chessboard.get(xytochess(x-1, y+color)).getpiece().equals(op + "P"))
        {
            check = true;
        }

    }
    /**
     * The king method looks for all the possible moves that king can perform and calls the kinghelper method to populate
     * vm arraylist with all the possible move
     *
     * @param position contains the position of the white or black king
     */
    public void king(String position)//looks for valid moves for king
    {
        int x = getx(position);//x position of king
        int y = gety(position);//y position of king

        kinghelper(add1, add1, x, y);//northeast
        kinghelper(add1, nochange, x, y);//east
        kinghelper(add1, minus1, x, y);//southeast
        kinghelper(nochange, add1, x, y);//north
        kinghelper(nochange, minus1, x, y);//south
        kinghelper(minus1, add1, x, y);//northwest
        kinghelper(minus1, nochange, x, y);//west
        kinghelper(minus1, minus1, x, y);//southwest

    }
    /**
     * The kinghelper method determines all the possible positions that the king king can move by process
     * of elimination. If a square is being threatened by an enemy piece, occupied by the players piece, or if
     * the square contains an enemy players piece but is being protected, this square is not deemed a possible move
     * for the king.
     *
     * @param m Lambda function that can increment, decrement, or do nothing to the x grid position of king
     * @param n Lambda function that can increment, decrement, or do nothing to the y grid position of king
     * @param x contains the x grid position of the king
     * @param y contains the y grid position of king
     */
    private void kinghelper(QRS m, QRS n, int x, int y)
    {
        x = m.QRSvalid(x);//increments or decrements or does not change x depending on lambda functions used
        y = n.QRSvalid(y);//increments or decrements or does not change y depending lambda functions used
        if(checkBounds(x, y))//see if the position is inbounds after being altered by the lambda function
        {
            if(chessboard.get(xytochess(x, y)) == null && !check(xytochess(x, y)))//king can go to empty square if not threatened by opposing piece
            {
                vm.add(xytochess(x, y));
            }
            if(chessboard.get(xytochess(x, y)) != null)
            {
                if(chessboard.get(xytochess(x, y)).getpiece().charAt(0) != cp)//checks to see if square does not contain players piece
                {
                    if(!check(xytochess(x, y)))//king can take opposing piece if not protected
                    {
                        vm.add(xytochess(x, y));
                    }

                }
            }
        }
    }

    /**
     * The knight method adds all the possible moves that a knight can perform and adds these moves to the "vm"
     * array list
     *
     * @param from contains the position of the white or black knight
     */
    private void knight(String from)
    {
        int x = getx(from);
        int y = gety(from);

        vm.add(xytochess(x+1, y+2));
        vm.add(xytochess(x-1, y+2));
        vm.add(xytochess(x-1, y-2));
        vm.add(xytochess(x+1, y-2));
        vm.add(xytochess(x+2, y+1));
        vm.add(xytochess(x-2, y+1));
        vm.add(xytochess(x+2, y-1));
        vm.add(xytochess(x-2, y-1));

    }

    /**
     * The bishop method adds all the possible moves that a bishop can perform and adds these moves to the "vm" array list,
     * only if this move is within the bounds of the chess board, there are no obstructing pieces in its path, and if
     * the space it wants to go to is either empty or has an opponent's piece in it
     *
     * @param from contains the position of the white or black bishop
     */
    private void bishop(String from)//all valid moves that are possible for bishop are added to the arraylist vm
    {
        int x = getx(from);
        int y = gety(from);

        int a = 1;
        int b = 1;

        while(checkBounds(x+a, y+b)) {
            if(chessboard.get(xytochess(x+a, y+b)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x+a, y+b));
                a++;
                b++;
            }
            else if(chessboard.get(xytochess(x+a, y+b)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x+a, y+b));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }


        int c = -1;
        int d = -1;

        while(checkBounds(x+c, y+d)) {
            if(chessboard.get(xytochess(x+c, y+d)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x+c, y+d));
                c--;
                d--;
            }
            else if(chessboard.get(xytochess(x+c, y+d)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x+c, y+d));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }


        int e = 1;
        int f = -1;

        while(checkBounds(x+e, y+f)) {
            if(chessboard.get(xytochess(x+e, y+f)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x+e, y+f));
                e++;
                f--;
            }
            else if(chessboard.get(xytochess(x+e, y+f)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x+e, y+f));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }

        int g = -1;
        int h = 1;

        while(checkBounds(x+g, y+h)) {
            if(chessboard.get(xytochess(x+g, y+h)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x+g, y+h));
                g--;
                h++;
            }
            else if(chessboard.get(xytochess(x+g, y+h)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x+g, y+h));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }
    }

    /**
     * The rook method adds all the possible moves that a rook can perform and adds these moves to the
     * "vm" array list, only if this move is within the bounds of the chess board, there are no obstructing pieces in its path,
     * and if the space it wants to go to is either empty or has an opponent's piece it
     *
     * @param from contains the position of white or black rook
     */
    private void rook(String from)//valid moves for rook
    {

        int x = getx(from);
        int y = gety(from);

        int a = 1;

        while(checkBounds(x+a, y)) {
            if(chessboard.get(xytochess(x+a, y)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x+a, y));
                a++;
            }
            else if(chessboard.get(xytochess(x+a, y)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x+a, y));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }

        int b = -1;

        while(checkBounds(x+b, y)) {
            if(chessboard.get(xytochess(x+b, y)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x+b, y));
                b--;
            }
            else if(chessboard.get(xytochess(x+b, y)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x+b, y));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }

        int c = 1;

        while(checkBounds(x, y+c)) {
            if(chessboard.get(xytochess(x, y+c)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x, y+c));
                c++;
            }
            else if(chessboard.get(xytochess(x, y+c)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x, y+c));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }

        int d = -1;

        while(checkBounds(x, y+d)) {
            if(chessboard.get(xytochess(x, y+d)) == null)//square is empty, it's a possible valid move, got to next square
            {
                vm.add(xytochess(x, y+d));
                d--;
            }
            else if(chessboard.get(xytochess(x, y+d)).getpiece().charAt(0) == op)//square contains enemy piece which is possible to capture break out of loop
            {
                vm.add(xytochess(x, y+d));
                break;
            }
            else{//square contains own piece break out of loop immediately its not a possible square to go
                break;
            }
        }
    }

    /**
     * The queen method adds all the possible moves that a bishop and a knight can perform and adds these moves to the
     * "vm" array list, only if this move is within the bounds of the chess board, there are no obstructing pieces in its path,
     * and if the space it wants to go to is either empty or has an opponent's piece in it. This is done by calling the bishop and rook methods
     *
     * @param from contains the position of the white or black queen
     */

    /**
     * The queen method adds all the possible moves that a bishop and a knight can perform and adds these moves to the
     * "vm" array list, only if this move is within the bounds of the chess board, there are no obstructing pieces in its path,
     * and if the space it wants to go to is either empty or has an opponent's piece in it. This is done by calling the bishop and rook methods
     *
     * @param from contains the position of the white or black queen
     */
    private void queen(String from)
    {
    	bishop(from);
        rook(from);
    }
    /**
     * The pawn method checks for all valid moves that the pawn can make via enpassant, capturing an enemy piece, or moving one space
     * or two spaces. The pawncounter and enpasant fields in ChessPiece object determines if a pawn can move 2 spaces or perform enpassant
     * respectively. A pawn counter that does not equal 1 mean this is not the pawns first move so it can't mvoe 2 spaces. An enpassant
     * boolean that equals true means that a pawn can capture a side enemy piece via enpassant.
     *
     * @param position contains the chess position of the pawn
     * @param color for white pawns color is +1 since they can only move positive direction, for black pawns color is -1 since they move in the negative y direction
     */
    private void pawn(String position, int color)
    {
        int x = getx(position);//x position of pawn
        int y = gety(position);//y position of pawn

        if(chessboard.get(xytochess(x, y+color)) == null)//checks if the front of pawn is empty space
        {
            if(chessboard.get(position).getPawnCounter() == 1 && chessboard.get(xytochess(x, y+2*color)) == null)// sees if 2 spaces from pawn is empty and pawns first move
            {
                vm.add(xytochess(x, y+2*color));//if this is pawns first move and there are 2 empty squares in front of it possible to move 2 spaced
            }
            vm.add(xytochess(x, y+color));//if this isn't pawns first moves pawn can only move one space
        }
        if(chessboard.get(xytochess(x+1, y+color)) != null && chessboard.get(xytochess(x+1, y+color)).getpiece().charAt(0) == op)//check if pawn can take opposing piece
        {
            vm.add(xytochess(x+1, y+color));//if so add enemy pieces position to vm as a possible space to move to
        }
        if(chessboard.get(xytochess(x-1, y+color)) != null && chessboard.get(xytochess(x-1, y+color)).getpiece().charAt(0) == op)
        {
            vm.add(xytochess(x-1, y+color));
        }
        if(chessboard.get(xytochess(x+1, y)) != null && chessboard.get(xytochess(x+1, y)).getpiece().equals(op + "P")//checks to see if enpassant on the right possible, main thing is if enemy pawn on isde has enpassant indicator set to true
        && chessboard.get(xytochess(x+1, y)).getenPassant() == true)
        {
            vm.add(xytochess(x+1, y+color));
        }
        if(chessboard.get(xytochess(x-1, y)) != null && chessboard.get(xytochess(x-1, y)).getpiece().equals(op + "P")//checks to see if enpassant on the left possible, main thing is if enemy pawn on isde has enpassant indicator set to true
        && chessboard.get(xytochess(x-1, y)).getenPassant() == true)
        {
            vm.add(xytochess(x-1, y+color));
        }

    }
    /**
     * The perfromMove method actually performs the move by the player. After the move is performed on the board
     * it is displayed unless it is a simulation move.
     *
     * @param from players starting postiion
     * @param to players destination position
     */
    public void performMove(String from, String to)
    {
        if(chessboard.get(from).getpiece().equals("wP"))//pawns move in a special way so they have own perform function
        {
            performpawnmove(from, to, wP);//wP is +1 since pawn move in position y direction
        }
        else if(chessboard.get(from).getpiece().equals("bP"))
        {
            performpawnmove(from, to, bP);//bP is -1 since pawn moves in negative ye direction
        }
        else//performs moves
        {
            chessboard.put(to, chessboard.get(from));//updates the HashMap chessboard with the new Chesspiece object in the new position
            chessboard.remove(from);//removes the old position
        }

    }
    /**
     * The performpawnmove method is called by the performMove method if the chesspiece is a pawn and perfroms the move. Pawns are special in that they can move to the corner
     * by one space, or move forward one space or move 2 spaces on their first move. They can also capture side enemy pawns in enPassant. The perfromapwnmove performs these moves
     * depending on what the destination position is (to). Also if a pawn moves 2 spaces and their are enemy pawns on its side, it's enpassant
     * boolean becomes true.
     *
     * @param from starting position of pawn
     * @param to destination position of pawn
     * @param color for white pawns color is +1 since they can only move positive direction, for black pawns color is -1 since they move in the negative y direction
     */
    private void performpawnmove(String from, String to, int color)
    {
        int xfrom = getx(from);//stores the x for the starting position of pawn
        int yfrom = gety(from);//stores the y for the starting position of pawn

        int xto = getx(to);//stores the x for the destination position of pawn
        int yto = gety(to);//stores the y for the destination position of pawn

        String[] promo = currentmove.split(" ");

        if(yto == yfrom + 2*color)//see if the move is to move two spaces
        {
            if(chessboard.get(xytochess(xfrom-1, yto)) != null &&chessboard.get(xytochess(xfrom-1, yto)).getpiece().equals(op+"P"))//if pawn moves 2 spaces
            //and their are 2 opponent pawns on either side it possible to do enpassant for the other side pawn so to let that opposing pawn know we set enpassant staus for the pawn to true
            {
                chessboard.get(from).enPassantStatus(true);
            }
            if(chessboard.get(xytochess(xfrom+1, yto)) != null && chessboard.get(xytochess(xfrom+1, yto)).getpiece().equals(op+"P"))
            {
                chessboard.get(from).enPassantStatus(true);
            }

        }
        else//for every other moves we set enpasant to false since enpassant only possible for after pawns first moves
        {
            chessboard.get(from).enPassantStatus(false);
        }
        if(yto == 8 || yto == 1)//promotional procedure for pawns if a pawn reaches the end of the board
        {
            if(promo.length != 3)//if player did not input a piece for promotion its is automaticaly taken as queen
            {
                chessboard.get(from).setPiece(cp + "Q");
            }
            else
            {
                chessboard.get(from).setPiece(cp + promo[2]);
            }

            chessboard.put(to, chessboard.get(from));
            chessboard.remove(from);
            return;


        }
        if(chessboard.get(xytochess(xfrom+1, yfrom)) != null && chessboard.get(xytochess(xfrom+1, yfrom)).getpiece().equals(op + "P")//checks if the pawn wants to perform enpassant
        && chessboard.get(xytochess(xfrom+1, yfrom)).getenPassant() == true && xfrom + 1 == xto && yto == yfrom + color)
        {
            chessboard.remove(xytochess(xfrom+1, yfrom));
        }
        if(chessboard.get(xytochess(xfrom-1, yfrom)) != null && chessboard.get(xytochess(xfrom-1, yfrom)).getpiece().equals(op + "P")//checks to se if pawn wants to perform enpassant
        && chessboard.get(xytochess(xfrom-1, yfrom)).getenPassant() == true && xfrom - 1 == xto && yto == yfrom + color)
        {
            chessboard.remove(xytochess(xfrom-1, yfrom));
        }

        //for all other non-special pawn moves
        chessboard.get(from).decrementPawnCounter();//pawncoutner is decrement so that it is not possibel to move 2 spaces after first move of pawn
        chessboard.put(to, chessboard.get(from));
        chessboard.remove(from);
        return;


    }
    /**
     * The chackmate method is used when a player's king is in check. It looks at all the pieces on the player's side and each of their possible moves.
     * Each move is simulated to see if this move gets the players king out of check. If there are moves moves that can get a king out of check, they are
     * added to the checkmoves arraylist. The player must input a move in the arraylist checkmoves otherwise reprompted to put another move.
     * If there are no moves possible to get the player's king out of check, that means checkmate is reached.
     *
     * @param king the position of current players king
     */
    public void checkmate(String king)//string king is the location of where this sides king is
    {
        checkmoves = new ArrayList<String>();//checkmoves stores all the moves possible when king is in check
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(chessboard.keySet());//storesa ll teh key


       for(String from: temp)//iterates over entire chessboard and looks at all the valid moves each piece can make and simulates the moves to see if helps king out of check
       {
            vm = new ArrayList<String>();
            if(chessboard.get(from) != null && chessboard.get(from).getpiece().charAt(0) == cp)//looks if pieces are of the current player
            {
                if(chessboard.get(from).getpiece().charAt(1) == 'N')
                {
                    knight(from);//checks all knights valid moves possible and populates them into vm
                    for(String to: vm)
                    {
                        simulate(from, to, king);//sees if one of those valid moves can take king out of check
                    }

                }
                else if(chessboard.get(from).getpiece().charAt(1) == 'B')
                {
                    bishop(from);//checks all bishop on players side possible moves and populates them into vm same [process for every piece]
                    for(String to: vm)
                    {
                        simulate(from, to, king);//sees if one of those valid moves can take king out of check
                    }
                }
                else if(chessboard.get(from).getpiece().charAt(1) == 'R')
                {
                    rook(from);//" "
                    for(String to: vm)
                    {
                        simulate(from, to, king);// " "
                    }

                }
                else if(chessboard.get(from).getpiece().charAt(1) == 'Q')
                {
                    queen(from);
                    for(String to: vm)
                    {
                        simulate(from, to, king);
                    }
                }
                else if(chessboard.get(from).getpiece().equals("wP"))
                {
                    pawn(from, wP);
                    for(String to: vm)
                    {
                        simulate(from, to, king);
                    }

                }
                else if (chessboard.get(from).getpiece().equals("bP"))
                {
                    pawn(from, bP);
                    for(String to: vm)
                    {
                        simulate(from, to, king);
                    }
                }
                else
                {
                    king(from);
                    for(String to: vm)
                    {
                        simulate(from, to, to);
                    }

                }


           }
       }
    }
    /**
     * The method simulate essentially simulates moves to see if the the move will get a king out of check. Every move that gets the king out of check is
     * stored in the checkmoves arraylist. Simulate can also test whether a move will cause a king to be check. For example if a pawn is protecting king
     * from being checked by enemy queen. The pawn is pinned and not able to move so simulate method will return true if the move will cause the king to be in check
     * if the pawn is moved making it an illegal move.
     *
     * @param from starting position of chesspiece
     * @param to destination position of chesspiece
     * @param king position of current players king
     * @return false if the current players king is no longer in check returns true if the current players king is still in check after simulated move
     */
    public boolean simulate(String from, String to, String king)
    {
        HashMap<String, ChessPiece> simulationMap = new HashMap<String, ChessPiece>();//stores the chessboard in temporary hashmap
        simulationMap.putAll(chessboard);
        int temp = 0;

        if(chessboard.get(from).getpiece().charAt(1) == 'P')//when performing pawn moves saves pawncounter before decrement
        {
            temp = chessboard.get(from).getPawnCounter();
        }
        performMove(from, to);//performs moves

        if(!check(king))//if after doing moves king is no longer in check it adds moves to checkmoves arraylist
        {
            checkmoves.add(from + " " + to);
            chessboard = simulationMap;// hashmap goes back to its state before we perform simulation move since we dont know what move user will use
            if(chessboard.get(from).getpiece().charAt(1) == 'P')
            {
                chessboard.get(from).setPawnCounter(temp);
            }
            return false;
        }
        else//kings is still in check after performing move, move not added to checkmoves
        {
            chessboard = simulationMap;
            if(chessboard.get(from).getpiece().charAt(1) == 'P')
            {
                chessboard.get(from).setPawnCounter(temp);
            }
            return true;
        }


    }
    /**
     * The main method creates a new Chess object and use it call the intialize, display, and start method
     * @param args used to read string input from console not used in this project
     */
    public static void main(String[] args)
    {
        Chess chess = new Chess();
        chess.initialize();
        chess.display();
        chess.start();

    }
    /**
     * The initialize method creates new ChessPiece obkects and populates them into the HAshMap chessboard.
     */
    public void initialize()
    {
        chessboard = new HashMap<String, ChessPiece>();
        //white
        ChessPiece WR = new ChessPiece("wR", 0, 1);
        chessboard.put("a1", WR);
        ChessPiece WN = new ChessPiece("wN", 0, 0);
        chessboard.put("b1", WN);
        ChessPiece WB = new ChessPiece("wB", 0, 0);
        chessboard.put("c1", WB);
        ChessPiece WQ = new ChessPiece("wQ", 0, 0);
        chessboard.put("d1", WQ);
        ChessPiece WK = new ChessPiece("wK", 0, 1);
        chessboard.put("e1", WK);
        ChessPiece WB2 = new ChessPiece("wB", 0, 0);
        chessboard.put("f1", WB2);
        ChessPiece WN2 = new ChessPiece("wN", 0, 0);
        chessboard.put("g1", WN2);
        ChessPiece WR2 = new ChessPiece("wR", 0, 1);
        chessboard.put("h1", WR2);
        ChessPiece WP1 = new ChessPiece("wP", 1, 0);
        chessboard.put("a2", WP1);
        ChessPiece WP2 = new ChessPiece("wP", 1, 0);
        chessboard.put("b2", WP2);
        ChessPiece WP3 = new ChessPiece("wP", 1, 0);
        chessboard.put("c2", WP3);
        ChessPiece WP4 = new ChessPiece("wP", 1, 0);
        chessboard.put("d2", WP4);
        ChessPiece WP5 = new ChessPiece("wP", 1, 0);
        chessboard.put("e2", WP5);
        ChessPiece WP6 = new ChessPiece("wP", 1, 0);
        chessboard.put("f2", WP6);
        ChessPiece WP7 = new ChessPiece("wP", 1, 0);
        chessboard.put("g2", WP7);
        ChessPiece WP8 = new ChessPiece("wP", 1, 0);
        chessboard.put("h2", WP8);

        //black
        ChessPiece BR = new ChessPiece("bR", 0, 1);
        chessboard.put("a8", BR);
        ChessPiece BN = new ChessPiece("bN", 0, 0);
        chessboard.put("b8", BN);
        ChessPiece BB = new ChessPiece("bB", 0, 0);
        chessboard.put("c8", BB);
        ChessPiece BQ = new ChessPiece("bQ", 0, 0);
        chessboard.put("d8", BQ);
        ChessPiece BK = new ChessPiece("bK", 0, 1);
        chessboard.put("e8", BK);
        ChessPiece BB2 = new ChessPiece("bB", 0, 0);
        chessboard.put("f8", BB2);
        ChessPiece BN2 = new ChessPiece("bN", 0, 0);
        chessboard.put("g8", BN2);
        ChessPiece BR2 = new ChessPiece("bR", 0, 1);
        chessboard.put("h8", BR2);
        ChessPiece BP1 = new ChessPiece("bP", 1, 0);
        chessboard.put("a7", BP1);
        ChessPiece BP2 = new ChessPiece("bP", 1, 0);
        chessboard.put("b7", BP2);
        ChessPiece BP3 = new ChessPiece("bP", 1, 0);
        chessboard.put("c7", BP3);
        ChessPiece BP4 = new ChessPiece("bP", 1, 0);
        chessboard.put("d7", BP4);
        ChessPiece BP5 = new ChessPiece("bP", 1, 0);
        chessboard.put("e7", BP5);
        ChessPiece BP6 = new ChessPiece("bP", 1, 0);
        chessboard.put("f7", BP6);
        ChessPiece BP7 = new ChessPiece("bP", 1, 0);
        chessboard.put("g7", BP7);
        ChessPiece BP8 = new ChessPiece("bP", 1, 0);
        chessboard.put("h7", BP8);

    }
    /**
     * The chesstonum method converts a chessposition to number from 1 - 64
     *
     * @param chessposition storest the chesspositoin eg e2
     * @return a number from 1 - 64 depending on the chessposition
     */
    public int chesstonum(String chessposition)//eg.e2 -> 11
    {
        return ((chessposition.charAt(0) - 96) + (Character.getNumericValue(chessposition.charAt(1)) - 1)*8);
    }
    /**
     * The xytochess method converts positon using x and y to a aplhanumeric chessposition
     *
     * @param x the x grid postion
     * @param y the y grid postion
     * @return the chesspositoin eg returns e2 from x = 5 y =2
     */
    public String xytochess(int x, int y)//eg.5,2 -> e2
    {
        return (char)('`' + x) + String.valueOf(y);
    }
    /**
     * The numtochess method returns the chessposition from the chesspieces number position eg returns h8 from 64
     *
     * @param position number position of chesspiece
     * @return chesspostion of chesspiece using its number position
     */
    public String numtochess(int position)//eg.11 -> e2
    {
        int x;
        int y;
        if(position % 8 == 0){
            x = 8;
            y = position/8;
        }else{
             y = position/8+1;
             x = position - ((y-1)*8);
        }

        return (char)('`' + x) + String.valueOf(y);

    }
    /**
     * The getx method returns the chessposition x grid position eg a2 -> 1
     *
     * @param chessposition stores the chesspostion
     * @return int value of the x position of the chesspostion
     */
    public int getx(String chessposition)//eg a2 -> 1
    {
        return chessposition.charAt(0) - 96;
    }
    /**
     * The gety method returns the chessposition y grid position eg a2 -> 2
     *
     * @param chessposition stores the chesspostion
     * @return int value of the y position of the chesspostion
     */
    public int gety(String chessposition)//eg a2 -> 2
    {
        return Character.getNumericValue(chessposition.charAt(1));
    }

    public void display()//prints board to console
    {
        System.out.println();
        int row = 7;
        int column = 1;
        while(row >= 0){
            String chessposition = numtochess(row*8 + column);
            if(chessboard.get(chessposition) == null){
                blank(row, column);
            }else{
                System.out.print(chessboard.get(chessposition).getpiece() + " ");
            }column++;
            if(column > 8){
                System.out.print(row + 1 + "\n");
                row--;
                column = 1;
            }

        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println();
    }
    private void blank(int row, int column)//prints blank squares
    {
        if(row % 2 == 0){
            if(column % 2 == 0){
                System.out.print("  " + " ");
            }else{
                System.out.print("##" + " ");
            }
        }else{
            if(column % 2 == 0){
                System.out.print("##" + " ");
            }else{
                System.out.print("  " + " ");
            }

        }
    }
    private boolean checkBounds(int x, int y)//checks if the x and y are in bounds of a standard chessboard
    {
        if(x>=1 && x<=8 && y>=1 && y<=8){
            return true;
        }else{
            return false;
        }
    }

}
