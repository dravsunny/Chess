package chess;

/**
 * The ChessPiece class stores the piece's name, a pawn counter used for pawns, and a castle counter for the rook and king. The ChessPiece object
 * is used as the value for the HashMap chessboard in teh Chess class.
 * The pawn counter is to keep track whether or not the move to performed by pawn is it's first move. If it is its first move it has
 * the option to move 2 spaces. The castle counter is used to see if a rook or king has moved. If the king has moved it is not possible to
 * castle. If the rook has moved, it is not possible to castle with that specific rook even if the king has not moved.
 *
 * @author Jaideep Duggempudi
 * @author Suneet Dravida
 *
 */
public class ChessPiece
{
    /**
     * chesspiece stores the color and the piece = eg wK mean white King
     */
    String chesspiece = null;
    /**
     * The pawn counter at the start is set to 1 for every pawn and 0 for all other pieces. If pawn moves its counter is decremented
     * meaning that it can no longer move 2 spaces anymore like it could if its pawncounter was 1
     */
    int pawncounter;
    /**
     * The castle counter is set to 1 for all kings and rooks and 0 for all other pieces. The castlecounter for a rook or king is decremented if the
     * piece is moved meaning the respective piece cannot be involved in castling.
     */
    int castlecounter;

    boolean enpassant = false;
    /**
     * The Counstructor defines what is the side/name of the piece, and the pawn counter, and castle couter
     * of the respective piece.
     *
     * @param chesspiece the name of the piece and what side it is on eg wR which dnotes white rook
     * @param pawncounter 1 for only pawns, 0 for the other pieces
     * @param castle 1 for kings and rooks, 0 for other pieces
     */
    public ChessPiece(String chesspiece, int pawncounter, int castle)
    {
        this.chesspiece = chesspiece;
        this.pawncounter = pawncounter;
        this.castlecounter = castle;

    }
    /**
     *
     * @return the String chesspiece eg wR
     */

    public String getpiece()
    {
        return chesspiece;
    }
    /**
     *
     * @param promotionto promotionto is the promotional piece that player wants to turn their pawn into when it reaches the end of the board
     */
    public void setPiece(String promotionto)
    {
        this.chesspiece = promotionto;
    }
    /**
     * If the pawncounter is 1it means the pawn hasn't moved yet it is its first move, otherwise returns 0 or is another piece
     * @return pawncounter to see if it's a pawns first move or not
     */
    public int getPawnCounter()
    {
        return pawncounter;
    }
    /**
     *
     * @param counter the new pawncounter that is to be set for the pawn
     */
    public void setPawnCounter(int counter)
    {
        this.pawncounter = counter;
    }
    /**
     * Decrements the pawncounter if the pawn has moved this method is called
     */
    public void decrementPawnCounter()
    {
        this.pawncounter--;
    }
    /**
     * Decrements the castlecounter if the rook or king has moved, this method is called
     */
    public void decrementcastleCounter()
    {
        this.castlecounter--;
    }
    /**
     *
     * @return castlecounter if it returns 1 it means the concerning king or rook hasnt moved, 0 if it has moved
     */
    public int getcastleCounter()
    {
        return castlecounter;
    }
    /**
     * This method determines if a pawn is able to eprform enpassant
     *
     * @return true is returned if the pawn can perform enpassant, false if it cannot
     */
    public boolean getenPassant()
    {
        return enpassant;
    }
    /**
     * This method can set a pawns enpasasnt statust as true if the conditions are met false otherwise
     *
     * @param status status is true if the pawn can perform enpasasnt false if it cannot
     */
    public void enPassantStatus(boolean status)
    {
        this.enpassant = status;
    }



}
