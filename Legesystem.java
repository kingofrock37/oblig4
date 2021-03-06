import java.util.*;
import java.io.*;

public class Legesystem {

  //Lager alle listene vi kan trenge i denne oppgaven
  static Liste<Pasient> pasientListe = new Lenkeliste<Pasient>();
  static Liste<Legemiddel> legemiddelListe = new Lenkeliste<Legemiddel>();
  static Liste<Lege> legeListe = new SortertLenkeliste<Lege>();
  static Liste<Resept> reseptListe = new Lenkeliste<Resept>();
  //I hvert bruk av disse listene brukes ikke for-each da dette ikke ble implementert av gruppemedlem før nær siste dag

  //Lager statistikk - tellere for programmet
  private static int antallVanedannende = 0;
  private static int antallNarkotiske = 0;

  public static void E3() {

    System.out.println("Alle pasienter\n");

      for (int i = 0; i < pasientListe.stoerrelse(); i++) {
        Pasient pasient = pasientListe.hent(i);
        System.out.println(pasient.toString());
      }


      System.out.println("Alle legemiddler\n");
      for (int i = 0; i < legemiddelListe.stoerrelse(); i++) {
        Legemiddel legemiddel = legemiddelListe.hent(i);
        System.out.println(legemiddel.toString());
      }

      System.out.println("Alle leger\n");
      for (int i = 0; i < legeListe.stoerrelse(); i++) {
        Lege lege = legeListe.hent(i);
        System.out.println(lege.toString());
      }
      System.out.println("Alle resepter\n");

      int count = 0;
      for (int i = 0; i < reseptListe.stoerrelse(); i++) {
        try {
          Resept resept = reseptListe.hent(i);
          System.out.println(resept.toString());
          count++;
        } catch(NullPointerException e) {
          System.out.println("Skipper NullPointerException" + e);
        }
      }
      System.out.println(count);
      return;
  }

  public static void E4() {
    doE4();
    return;
  }

  public static void E5() {
    brukResept();
    return;
  }

  public static void E6() { //Statistikk
    //Alle disse hadde vært lettere med iterator, men oen oppgaven ble gjort etter denne
    //Printer antall vanedannende og narkotiske

    System.out.println("Reseptliste size: " + reseptListe.stoerrelse());
    try {
      for (int m = 0; m < reseptListe.stoerrelse(); m++) {
        if (reseptListe.hent(m) != null && reseptListe.hent(m).hentLegemiddel() instanceof Narkotisk) {
          antallNarkotiske++;
        } else if (reseptListe.hent(m) != null && reseptListe.hent(m).hentLegemiddel() instanceof Vanedannende) {
          antallVanedannende++;
        }
      }
    } catch(NullPointerException e) {
      System.out.println("Feil i antallnarkotiske");
    }

    System.out.println("\nAntall Narkotiske Resepter: " + antallNarkotiske);
    System.out.println("Antall Vanedannende Resepter: " + antallVanedannende + "\n");

    //Printer alle leger som har skrevet ut narkotiske, inkl hvor mange
    System.out.println("Leger:");
    for (int i = 0; i < legeListe.stoerrelse(); i++) {

      Lege tempLege = legeListe.hent(i);
      int tempLegeNark = 0;
      Liste<Resept> tempLegeListe = tempLege.hentUtskrevedeResepter();

      for (int j = 0; j < tempLegeListe.stoerrelse(); j++) {
        if (tempLegeListe.hent(j).hentLegemiddel() instanceof Narkotisk) {
          tempLegeNark++;
        }
      }

      if (tempLegeNark > 0) {
        System.out.println("Lege " + tempLege.hentNavn() + " har skrevet ut " + tempLegeNark
         + " narkotiske legemidler");
      }
    }

    //Printer alle pasienter som har fått narkotiske, inkl hvor mange
    System.out.println("\nPasienter:");
    for (int k = 0; k < pasientListe.stoerrelse(); k++) {
      Pasient tempPas = pasientListe.hent(k);
      int tempPasNark = 0;
      Liste<Resept> tempPasListe = tempPas.hentResepter();

      for (int l = 0; l < tempPasListe.stoerrelse(); l++) {
        if (tempPasListe.hent(l).hentLegemiddel() instanceof Narkotisk) {
          tempPasNark++;
        }
      }
      if (tempPasNark > 0) {
        System.out.println("Pasient " + tempPas.hentNavn() + " har faatt utskrevet "
        + tempPasNark + " narkotiske resepter");
      }
    }
    System.out.println("\n");

  }

  public static void E8() { //Skriv til fil

    FileWriter w = null;
    PrintWriter writer = null;
    try {
      w = new FileWriter("utfil.txt");
      writer = new PrintWriter(w);

      int i = 0;
      int j = 0;
      int k = 0;
      int l = 0;

      writer.println("# Pasienter (navn,fnr)");
      while (i < pasientListe.stoerrelse()) {
        Pasient temp = pasientListe.hent(i);
        writer.println(temp.hentNavn() + "," + temp.hentFodselsnr());
        i++;
      }

      writer.println("# Legemidler (navn,type,pris,virkestoff,[styrke])");
      while (j < legemiddelListe.stoerrelse()) { //Må downcaste temp for å få tilgang til metoder i subklasser i denne og neste
        Legemiddel temp = legemiddelListe.hent(j);
        if (temp instanceof Vanlig) {
          writer.println(temp.hentNavn() + "," + "vanlig" + "," + temp.hentPris() + "," + temp.hentVirkestoff());
        } else if (temp instanceof Narkotisk) {
          writer.println(temp.hentNavn() + "," + "narkotisk" + temp.hentPris() + ","
          + temp.hentVirkestoff() + "," + ((Narkotisk)temp).hentNarkotiskStyrke());
        } else if (temp instanceof Vanedannende) {
          writer.println(temp.hentNavn() + "," + "vanedannende" + "," + temp.hentPris() + ","
          + temp.hentVirkestoff() + "," + ((Vanedannende)temp).hentVanedannendeStyrke());
        }
        j++;
      }

      writer.println("# Leger (navn,kontrollid / 0 hvis vanlig lege)");
      while (k < legeListe.stoerrelse()) {
        Lege temp = legeListe.hent(k);
        if (temp instanceof Spesialist) {
          writer.println(temp.hentNavn() + "," + ((Spesialist)temp).hentKontrollID());
        } else if (temp instanceof Lege) {
          writer.println(temp.hentNavn() + "," + 0);
        }
        k++;
      }

      writer.println("# Resepter (legemiddelNummer,legeNavn,pasientID,type,[reit])");
      while (l < reseptListe.stoerrelse()) {
        try {
          Resept temp = reseptListe.hent(l);
          if (temp instanceof PResept) {
            writer.println(temp.hentLegemiddel().hentId() + "," + temp.hentLege().hentNavn()
             + "," + temp.hentPasientId() + "," + "p");
          } else if (temp instanceof MilitaerResept) {
            writer.println(temp.hentLegemiddel().hentId() + "," + temp.hentLege().hentNavn()
             + "," + temp.hentPasientId() + "," + "militaer" + "," + temp.hentReit());
          } else if (temp instanceof HvitResept) {
            writer.println(temp.hentLegemiddel().hentId() + "," + temp.hentLege().hentNavn()
             + "," + temp.hentPasientId() + "," + "hvit" + "," + temp.hentReit());
          } else if (temp instanceof BlaaResept) {
            writer.println(temp.hentLegemiddel().hentId() + "," + temp.hentLege().hentNavn()
             + "," + temp.hentPasientId() + "," + "blaa" + "," + temp.hentReit());
          }
          l++;
        } catch (Exception e) {
          System.out.println(e + " | Feilet aa skrive resept - Ugyldig legemiddel?");
          l++;
        }
      }
      System.out.println("\nSkrevet til fil!\n");
    } catch (IOException e) {
      System.out.println(e);
    } finally {
      writer.close();
    }

  }

  public static void main(String[] args){
    //Laster inn myeinndata.txt
    File innFil = new File("myeinndata.txt");
    lesFraFil(innFil);
    boolean avslutt = false;
    String input;
    int valg = 0;
    Scanner scan = new Scanner(System.in);

    while (!avslutt){
      System.out.println("Vennligst velg en av disse valgmulighetene:"
        + "\n1. Skrive ut en fullstendig oversikt over pasienter, leger, legemidler og resepter"
        + "\n2. Opprette og legg til nye elementer i systemet."
        + "\n3. Bruke en gitt resept fra listen til en pasient."
        + "\n4. Skrive ut forskjellige former for statistikk."
        + "\n5. Skrive all data til fil."
        + "\n0. Avslutt program.");
      try {
        input = scan.next();
        valg = Integer.parseInt(input);
        // Cases går til funksjonalitene i oppgavene
        switch (valg){
          case 1:
            E3();
            break;
          case 2:
            E4();
            break;
          case 3:
            E5();
            break;
          case 4:
            E6();
            break;
          case 5:
            E8();
            break;
          case 0:
            avslutt = true;
            break;
          default:
            System.out.println("Det er ikke et gyldig valg.");
        }
      }
      catch(Exception e) {
        System.out.println(e);
        System.out.println("Det er ikke et gyldig valg.");
      }


    }

  }

  private static void brukResept() {
    int antallPasienter = 0;
    Scanner scanner = new Scanner(System.in);

      //Melding til bruker om at ingen pasienter er lagt til i liste
      if(pasientListe.stoerrelse() <= 0) {
        System.out.println("Ingen pasienter funnet");
        return;
      }

      //--------PASIENT---------
      System.out.println("Hvilken pasient vil du se resept for?");

      //Skriver ut pasienter bruker kan velge, tar inn valgt pasient
      for(Pasient pasient : pasientListe) {
        System.out.println(pasient.hentId() + ": " + pasient.hentNavn() + "(fnr " + pasient.hentFodselsnr() + ")");
        antallPasienter++;
      }

      int valgtPasient;

      //Feilsjekking av input fra bruker
      try {
        valgtPasient = scanner.nextInt();
      }catch (NumberFormatException e){
        valgtPasient = -1;
      }

      //-----RESEPT------
      if(valgtPasient >= 0 && valgtPasient < antallPasienter) {
          Pasient pasient = pasientListe.hent(valgtPasient);

          if(pasient.hentResepter().stoerrelse() == 0) {
              System.out.println("Pasienten har ingen resepter. Gaar tilbake til hovedmeny");
          }
          else {
              System.out.println("Valgt pasient: " + pasient.hentNavn() + "(fnr" + pasient.hentFodselsnr() + ")\n" + "Hvilken resept vil du bruke?");

              int antalltResepter = 0;

              for (Resept resept : pasient.hentResepter()) {
                  System.out.println(antalltResepter + ": " + resept.hentLegemiddel().hentNavn() + " (" + resept.hentReit() + " reit)");
                  antalltResepter++;
              }

              int valgtResept;

              try {
                  valgtResept = scanner.nextInt();
              } catch (NumberFormatException e) {
                  valgtResept = -1;
              }

              Resept oensketResept = pasient.hentResepter().hent(valgtResept);

              try {
                  pasient.resepter.hent(valgtResept).bruk();

                  System.out.println("Brukte resept paa " + oensketResept.hentLegemiddel().hentNavn() + ". Antall gjenværende reit: " + oensketResept.hentReit());

              } catch (Exception e) {
                  System.out.println("Kunne ikke bruke resept paa " + oensketResept.hentLegemiddel().hentNavn() + " (ingen gjenvaerende reit).");
              }
          }
      } else {
          System.out.println("Ugyldig valg. Gaar tilbake til hovedmeny");
      }

  }
  //Les-fra-fil metode
  private static void lesFraFil(File fil) {
    //Fikk opprinnelig en uforståelig feil knyttet til lesing av filen inndata.txt som
    //Jeg fikset ved å laste ned inndata.txt på nytt. Vet ikke om dette kan vedvare?
    Scanner scanner = null;
    System.out.println("\nLeser fra fil:\n");

    try {
      scanner = new Scanner(fil);
    } catch(FileNotFoundException e) {
      System.out.println("Ingen fil funnet");
      return;
    }

    String innlest = scanner.nextLine();

    while(scanner.hasNextLine()) {

      String[] info = innlest.split(" ");

      //Sjekker hvilket objekt som skal lages utifra informasjon i fil, og lager tilsvarende objekt
      if (info[1].compareTo("Pasienter") == 0) {

        while(scanner.hasNextLine()) {

          try {
            innlest = scanner.nextLine();
            info = innlest.split(",");

            if (innlest.charAt(0) == '#') {
              break;
            }
            //Pasient(String navn, int fnr);
            String navn = info[0];
            String fnr = info[1];

            Pasient nyPasient = new Pasient(navn, fnr);
            pasientListe.leggTil(nyPasient);
          } catch(Exception e) {
            System.out.println("Ignorerer Ugyldig Pasient. Forventet (navn, fnr), mottok " + info[0] + "," + info[1]);
          }

        }

      }
      //Legger inn Legemidlene
      else if(info[1].compareTo("Legemidler") == 0){

          while(scanner.hasNextLine()){

            try {
              innlest = scanner.nextLine();
              info = innlest.split(",");
              Legemiddel x;

              if(innlest.charAt(0) == '#'){
                  break;
              }
              //Legemiddel(String navn, Type type, double pris, double virkestoff, int [styrke]);
              String navn = info[0];
              Double pris = Double.valueOf(info[2]);
              Double virkestoff = Double.valueOf(info[3]);
              int styrke;

              String[] legemiddel = innlest.split(",");
              if(legemiddel[1].compareTo("vanlig") == 0){
                x = new Vanlig(navn, pris, virkestoff);
                legemiddelListe.leggTil(x);

              } else if(legemiddel[1].compareTo("vanedannende") == 0){
                styrke = Integer.parseInt(info[4]);
                x = new Vanedannende(navn, pris, virkestoff, styrke);

                legemiddelListe.leggTil(x);

              } else if (legemiddel[1].compareTo("narkotisk") == 0){
                styrke = Integer.parseInt(info[4]);
                x = new Narkotisk(navn, pris, virkestoff, styrke);

                legemiddelListe.leggTil(x);

              }
            } catch(Exception e) {
              String toPrint = "";
              for(String i : info)
                toPrint += i + " | ";
              System.out.println("Ignorerer Ugylidg Legemiddel. Forventet (navn, type, pris, virkestoff, [styrke]), mottok " + toPrint);
            }

          }

      }
      //Legger inn leger
      else if(info[1].compareTo("Leger") == 0){

          while(scanner.hasNextLine()) {

            try {
              innlest = scanner.nextLine();
              info = innlest.split(",");
              Lege x;

              if(innlest.charAt(0) == '#'){
                  break;
              }
              //Lege(String navn, int [kontrollId])
              String navn = info[0];
              int kontrollid = Integer.parseInt(info[1]);

              if(kontrollid == 0){

                x = new Lege(navn);
                legeListe.leggTil(x);

              } else {

                x = new Spesialist(navn, kontrollid);
                legeListe.leggTil(x);

              }

            } catch(Exception e) {
              String toPrint = "";
              for(String i : info)
                toPrint += i + " | ";
              System.out.println("Ignorerer Ugyldig Lege. Forventet (navn, [kontrollId]), mottok " + toPrint);
            }

          }

      }
      //Legger inn Resepter
      else if(info[1].compareTo("Resepter") == 0){

        while(scanner.hasNextLine()){

          try {
            innlest = scanner.nextLine();
            info = innlest.split(",");
            //Resepter(legemiddelNr, legeNavn, pasientId, type, [reit])

            Legemiddel legemiddel = null;
            Lege lege = null;
            Pasient pasient = null;
            int reit = 0;

            Resept nyResept = null;

            //Finner legemiddel
            for (int i = 0; i < legemiddelListe.stoerrelse(); i++) {
              if (legemiddelListe.hent(i).hentId() == Integer.parseInt(info[0])) {
                legemiddel = legemiddelListe.hent(i);
                break;
              }
            }

            //Finner lege
            for (int i = 0; i < legeListe.stoerrelse(); i++) {
              if (legeListe.hent(i).hentNavn().compareTo(info[1]) == 0) {
                lege = legeListe.hent(i);
                break;
              }
            }

            //Finner pasient
            for (int i = 0; i < pasientListe.stoerrelse(); i++) {
              if (pasientListe.hent(i).hentId() == Integer.parseInt(info[2])) {
                pasient = pasientListe.hent(i);
                break;
              }

            }

            //Sjekker type og lar lege skrive ny resept.
              try {
                if (info[3].compareTo("p") == 0) {
                  nyResept = lege.skrivPResept(legemiddel, pasient);
                } else {
                  reit = Integer.parseInt(info[4]);
                }

                if (info[3].compareTo("blaa") == 0) {
                  nyResept = lege.skrivBlaaResept(legemiddel, pasient, reit);
                } else if (info[3].compareTo("hvit") == 0) {
                  nyResept = lege.skrivHvitResept(legemiddel, pasient, reit);
                } else if (info[3].compareTo("millitaer") == 0) {
                  nyResept = lege.skrivMilitaerResept(legemiddel, pasient, reit);
                }

                reseptListe.leggTil(nyResept);
                //System.out.println(nyResept.hentLegemiddel().hentNavn() + " " + lege.hentNavn() + " " + nyResept.hentPasientId());
              } catch(UlovligUtskrift e) {}

          } catch(Exception f) {
            String toPrint = "";
            for(String i : info)
              toPrint += i + " | ";
            System.out.println("Ignorerer Ugyldig Resept. Forventet (legemiddelNr, legeNavn, type, [reit]), mottok " + toPrint);
          }

        }

      }

    }
    System.out.println("Fil " + fil.getName() + " lest! Antall lest fra fil:");
    System.out.println("Pasienter: " + pasientListe.stoerrelse());
    System.out.println("Legemidler: " + legemiddelListe.stoerrelse());
    System.out.println("Leger: " + legeListe.stoerrelse());
    System.out.println("Resepter: " + reseptListe.stoerrelse());
    System.out.println("");

  }
   //Man, E4. LeggtilPasient,Leger,Legemiddler, og Resepter
  public static void doE4(){
    Scanner input2 = new Scanner (System.in);

    System.out.println("oonsker du aa legge til: ");
    System.out.println("Lege, tast 1");
    System.out.println("Pasient, tast 2");
    System.out.println("Legemiddel, tast 3");
    System.out.println("Rasept, tast 4");
    String leggTilValg = input2.nextLine();


    if (leggTilValg.equals("1")){
      leggTilLege();
    } else if (leggTilValg.equals("2")) {
      leggTilPasient();
    } else if (leggTilValg.equals("3")) {
      leggTilLegemiddel();
    } else if (leggTilValg.equals("4")) {
      leggTilResept();
    } else {
      System.out.println("Du har skrevet ugyldig input");
    }
  }


  public static void leggTilLege(){
    Scanner nylegescan = new Scanner(System.in);

    System.out.println("Hva er legens navn: ");
    String navn = nylegescan.nextLine();

    System.out.println("Er denne legen spesialist");
    System.out.println("Tast 1 for ja");
    System.out.println("Tast 2 for nei");
    String legeType = nylegescan.nextLine();


    if (legeType.equals("1")) {
      System.out.println("Tast inn et kontrollid ");
      int kontrollid = nylegescan.nextInt();
      nylegescan.nextLine();
      Lege nyLege = new Spesialist(navn, kontrollid);
      legeListe.leggTil(nyLege);
    } else if (legeType.equals("2")){
      Lege nyLege = new Lege(navn);
      legeListe.leggTil(nyLege);
    } else {
      System.out.println("Du har skrevet ugyldig input");
    }
    System.out.println("Legen ble oprettet.");
  }


  public static void leggTilPasient(){
    Scanner nyPasientInput = new Scanner(System.in);

    System.out.println("Hva er navnet til pasienten: ");
    String navn = nyPasientInput.nextLine();

    System.out.println("Hva er pasientens foodselsnummer: ");
    String foodselsnummer = nyPasientInput.nextLine();

    Pasient nyPasient = new Pasient(navn, foodselsnummer);
    pasientListe.leggTil(nyPasient); //navnet på objektet, trenger info
    System.out.println("Pasienten ble oprettet");
  }


  public static void leggTilLegemiddel(){
    Scanner nyLegemidler = new Scanner(System.in);

    System.out.println("Hva slags type legemiddel er det?");
    System.out.println("Skriv 1 for Narkotisk legemiddel");
    System.out.println("Skriv 2 for Vanedannende");
    System.out.println("Skriv 3 for Normal legemiddel");

    String typeLegemiddler = nyLegemidler.nextLine();

    if (typeLegemiddler.equals("1")) {
      System.out.println("Hva er navnet til Narkotisk legemiddel: ");
      String navn = nyLegemidler.nextLine();

      System.out.println("Hva er prisen til Narkotisk legemiddel: ");
      double pris = nyLegemidler.nextDouble();
      nyLegemidler.nextLine();

      System.out.println("Hvor mye Virkestoff(mg) i Narkotisk legemiddelet: ");
      double virkestoff = nyLegemidler.nextDouble();
      nyLegemidler.nextLine();

      System.out.println("Narkotisk styrke: ");
      int styrke = nyLegemidler.nextInt();
      nyLegemidler.nextLine();

      Narkotisk narkotisk = new Narkotisk(navn, pris, virkestoff, styrke);
      legemiddelListe.leggTil(narkotisk);
      System.out.println("Narkotisk Legemidlet ble oprettet");


    } else if (typeLegemiddler.equals("2")){
      System.out.println("Hva er navnet til vanedannende leggemiddel: ");
      String navn = nyLegemidler.nextLine();

      System.out.println("Hva er prisen til vanedannende legemiddel: ");
      double pris = nyLegemidler.nextDouble();
      nyLegemidler.nextLine();

      System.out.println("Hvor mye virkestoff(mg) i vanedannende legemiddel");
      double virkestoff = nyLegemidler.nextDouble();
      nyLegemidler.nextLine();

      System.out.println("Vannedanende styrke: ");
      int styrke = nyLegemidler.nextInt();
      nyLegemidler.nextLine();

      Vanedannende vanedannende = new Vanedannende(navn, pris, virkestoff, styrke);
      legemiddelListe.leggTil(vanedannende);
      System.out.println("Vanedannende legemidlet ble oprettet");


    } else if (typeLegemiddler.equals("3")){
      System.out.println("Hva er navnet til vanlig leggemiddel: ");
      String navn = nyLegemidler.nextLine();

      System.out.println("Hva er prisen til vanlig legemiddel: ");
      double pris = nyLegemidler.nextDouble();
      nyLegemidler.nextLine();

      System.out.println("Hvor mye virkestoff(mg) i normal legemiddel ");
      double virkestoff = nyLegemidler.nextDouble();
      nyLegemidler.nextLine();

      Vanlig legemiddel = new Vanlig(navn, pris, virkestoff);
      legemiddelListe.leggTil(legemiddel);
      System.out.println("Vanlig legemidlet ble oprettet");
    } else {
      System.out.println("Du har skrevet ugyldig input");
    }
  }
  //kodeblokken

  public static void leggTilResept(){
    Scanner nyReseptScanner = new Scanner(System.in);

    if (pasientListe.stoerrelse() == 0) {
      System.out.println("Det finnes ingen pasienter. Resept kan ikke oprettes.");
      return;
    }

    System.out.println("Hvilken pasient skal utskrives en resept til?");
    for (Pasient pasient : pasientListe) {
      System.out.println(pasient.toString());
    }

    Pasient pasient = null;
    int pasientId = nyReseptScanner.nextInt();
    nyReseptScanner.nextLine();

    for (Pasient a : pasientListe) {
      if (a.hentId() == pasientId) {
        pasient = a;
      }
    }
    if (pasient == null) {
      System.out.println("Fant ikke pasienten!");
      return;
    }

    if (legeListe.stoerrelse() == 0){
      System.out.println("Det finnes ingen leger! Resept kan ikke oprettes.");
      return;
    }

    System.out.println("Hvilken lege utskriver denne resepten? ");
    for (Lege lege : legeListe) {
      System.out.println(lege.toString());
    }

    Lege lege = null;
    String legeNavn = nyReseptScanner.nextLine();

    for (Lege b : legeListe) {
      if (b.hentNavn().equals(legeNavn)){
        lege = b;
      }
    }
    if (lege == null) {
      System.out.println("Fant ikke legen!");
      return;
    }

    if (legeListe.stoerrelse() == 0){
      System.out.println("Det finnes ingen legemidler! Resept kan ikke oprettes.");
      return;
    }

    System.out.println("Hvilket legemiddel skal brukes?");
    for (Legemiddel legemiddel : legemiddelListe) {
      System.out.println(legemiddel.toString());
    }

    Legemiddel legemiddel = null;
    String legemiddelNavn = nyReseptScanner.nextLine();

    for (Legemiddel c : legemiddelListe) {
      if (c.hentNavn().equalsIgnoreCase(legemiddelNavn)) {
        legemiddel = c;
      }
    }
    if (legemiddel == null) {
      System.out.println("Fant ikke legemiddel!");
      return;
    }

    System.out.println("Hvor mange ganger kan resepten brukes?");
    int reit = nyReseptScanner.nextInt();
    nyReseptScanner.nextLine();

    System.out.println("Hva slags type resept er det? ");
    System.out.println("er det:");
    System.out.println("blaa Resept, tast 1");
    System.out.println("P resept, tast 2");
    System.out.println("Militaer resept, tast 3");

    String input3 = nyReseptScanner.nextLine();

    if(input3.equals("1")) {
      try {
        Resept blaaResept = lege.skrivBlaaResept(legemiddel, pasient, reit);
        pasient.leggTilResept(blaaResept);
        reseptListe.leggTil(blaaResept);
        System.out.println("Resepten ble lagret");
      } catch(UlovligUtskrift e) {
        System.out.println(e);
      }
    } else if (input3.equals("2")) {
      try {
        Resept pResept = lege.skrivPResept(legemiddel, pasient);
        pasient.leggTilResept(pResept);
        reseptListe.leggTil(pResept);
        System.out.println("Resepten ble lagret");
      } catch (UlovligUtskrift e) {}
    } else if (input3.equals("3")){
      try {
        Resept mResept = lege.skrivMilitaerResept(legemiddel, pasient, reit);
        pasient.leggTilResept(mResept);
        reseptListe.leggTil(mResept);
        System.out.println("Resepten ble lagret");
      } catch (UlovligUtskrift e) {}
    } else {
      System.out.println("Du har skrevet feil input.");
    }

  }

}
