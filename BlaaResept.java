class BlaaResept extends Resept {
  //Test
  public BlaaResept(Legemiddel lm,  Lege ul, Pasient p, int reit) {
    super(lm, ul, p, reit);
  }

  public String farge() {
    return "BLAA";
  }

  public double prisAaBetale() {
    return legemiddel.hentPris() * 0.25;
  }

  @Override
  public String toString() {
    return "BLAARESEPT\n" + "Farge: " + this.farge() + " Legemiddel: "
     + legemiddel.toString() + "Lege: " + utskrivendeLege.toString() + "\nReseptId: "
      + this.hentId() + " PasientId: " + this.hentPasientId() + " Pris aa betale: "
       + this.prisAaBetale() + " Reit: " + this.hentReit() + "\n";
  }
}
