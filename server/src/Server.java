import database.Database;

/**
 * Server class, this is less than a skeleton at the moment...
 */
public class Server{
    public static void main(String[] args){
        Database db = new Database();
        System.out.printf(db.getGolbalTopTen());
        db.closeConnection();
    }
}
