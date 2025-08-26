package eft.ldwtoros.dto;

public class CancelarParcialItem {
    private String secction;  // p.ej "217"
    private String row;       // p.ej "B"
    private Long seat;        // p.ej 20

    public String getSecction() { return secction; }
    public void setSecction(String secction) { this.secction = secction; }
    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }
    public Long getSeat() { return seat; }
    public void setSeat(Long seat) { this.seat = seat; }
}
