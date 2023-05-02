package control;

import control.db.UploadService;

import java.sql.SQLException;

public class UploadSummoner implements Runnable{
    @Override
    public void run() {
        try {
            UploadService.upload();
        }catch(SQLException e){
            try {
                Controller.getInstance().load();
            } catch (SQLException ex) {
                System.exit(0);
            }
        }
    }
}
