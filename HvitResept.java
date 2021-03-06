class HvitResept extends Resept {

  public HvitResept(Legemiddel lm,  Lege ul, Pasient p, int reit) {
    super(lm, ul, p, reit);
  }

  public String farge() {
    return "HVIT";
  }

  public double prisAaBetale() {
    return legemiddel.hentPris();
  }

  @Override
  public String toString() {
    return "HVITRESEPT\n" + "Farge: " + this.farge() + " Legemiddel: "
     + legemiddel.toString() + "Lege: " + utskrivendeLege.toString() + "\nReseptId: "
      + this.hentId() + " PasientId: " + this.hentPasientId() + " Pris aa betale: "
       + this.prisAaBetale() + " Reit: " + this.hentReit() + "\n";
  }
}
