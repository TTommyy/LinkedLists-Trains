//Tomasz Koczar-5

import java.util.Scanner;

/*Idea rowzwiazania:
    Mamy 3 klasy:
        Car - reprezentuje wagon.
        Train - dwukierunkowa cykliczna lista wiazana wagonow, reprezentuje pociag.
        TrainList - jednokierunkowa lista - przechowujemy w niej pociagi.
    Oprocz wyszukiwania, wypsiwania
    wszystkie polecenia wykonuja sie
    w czasie stalym. Obracanie przepina referencje tak aby przy wyswietlaniu zorientowac sie czy pociag nie jest obrocony
    i gdy jest obrocony wypisujemy go poprawnie przy wypisywaniu

 */

 /*
 Test dolaczony do tresci zadania zwraca:
   T1: W1 
    Train T1 already exists
    T1: W1 W2 
    T1: W1 
    T2: W2 
    T3: W1 
    T2: W2 
    Train T1 does not exist
    Trains: T3 T2 
    Co jest zgodne z oczekiwaniami.
  */


class TrainList{
    /*************************************************************************************/


    /**Klasa obslugujaca Pociag */
    public class Train{
        /*****************************************************/
        /**Klasa obslugujaca wagon */
        private class Car{
            /*----Pola---- */
            private String name;
            private Car next;
            private Car prev;
            /*---Metody----*/
            /**Konsturktor */
            private Car(String name){
                this.name = name;
            }

            /**Odwraca referencje w wagonie */
            public void reverse(){
                Car temp = next;
                next = prev;
                prev = temp;
            }
            /**Do testow*/
            public void present(){
                System.out.println("Hej, jestem: "+name+ ". Moj next to: " + next.name +", a prev to: " + prev.name);
            }
        }
        /*****************************************************/
        /**Pola klasy Train */
        private Car first;//pierwszy wagon
        private Train next;//referecja do nastepnego pociagu
        private String name;//nazwa pociagu

        /**Konstruktor */
        private Train(String name, String carName){
            this.name = name;
            first = new Car(carName);
            first.next = first;
            first.prev = first;
            next = null;
        }
        /**Sprawdza czy nazwa wagonu nie jest juz w uzyciu */
        private boolean isNameAvaiable(String name){ //dla zwiekszenia wydajnosci nie bedziemy tego uzwyac
            Car temp = first;
            do{
                if(temp.name.equals(name))
                    return false;
                temp = temp.next;
            } while(temp!=first);
            return true; 
        };
        /**Dodaje wagony na poczatek pociagu */
        public boolean pushFirst(String name){
            //if(isNameAvaiable(name)){

                Car temp =new Car(name);
                Car temp2 = first.prev;
                temp.next = first;
                first.prev.next = temp;
                first.prev = temp;
                first = temp;
                temp.prev = temp2;
                return true;
            //}
            //System.out.println("Car name already exist");
            //return false;
        }
        /**Dodaje wagony na koniec pociagu */
        public boolean pushLast(String name){
            //if(isNameAvaiable(name)){
                
                if(first==null){
                    System.out.println("Robisz cos zle w psuh last");
                    return false;
                }
                Car temp = first.prev;
                temp.next = new Car(name);
                temp.next.prev = temp;
                temp.next.next = first;
                first.prev = temp.next;
                return true;
            //}
            //System.out.println("Car name already exist");
            //return false;
        }

        /**Odwraca kolejnosc wagonow */
        /*
        Wersja poprawniejsza aczkolwiek dzialajaca 
        w czasie liniowym O(n). 
        public void reverseCars(){
            Car temp,temp2;
            temp = first;
            do{
                temp2 = temp.next;
                temp.next = temp.prev;
                temp.prev = temp2;
                temp = temp2;
            }while( temp != first);
            first = first.next;
        }*/
        
        public void reverseCars(){
            Car temp = first.prev;
            first.reverse();//odwracamy pierwszy wagon
            temp.reverse();//odwracamy nastpeny po odwroconym pierwszym czyli temp
            first = temp;//a ostatni beda pierwszymi
        }

        /**Wyszukuje wagon w pociagu */
        public Car search(String n){
            Car temp = first;
            if(temp.name.equals(n)) return temp;
            while(temp.next!= first){
                if(temp.name.equals(n)) return temp;
                temp = temp.next;
            }
            return null;
        }
        /**Usuwa pierwszy wagon i zwraca jego nazwe */
        public String delateFirst(){
            Car temp = first;


            if(first.next == first){//jeden wagon
                first = null;
            }

            else if(first.next.next == first){// gdy dwa wagony
                first = temp.next;
                first.next = first.prev = first;
            }
            else {   
                temp.next.prev = temp.prev;
                temp.prev.next = temp.next;
                first = temp.next;
            }
            return temp.name;
        }

        /**Usuwa ostatni wagon i zwraca jego nazwe */
        public String delateLast(){
            Car temp = first.prev;

            if(first.next.next == first){// gdy dwa wagony
                first.next = first.prev = first;
            
            }

            else if(first!=first.next){
                temp.prev.next = first;
                first.prev =temp.prev;
            }
            else first = null;
            return temp.name;
        }

        /**Laczy dwa pociagi */
        public void merge(Train train){

            Car temp = first.prev;
            first.prev = train.first.prev;
            first.prev.next = first;
            temp.next = train.first;
            temp.next.prev = temp;
            //temp.present();
        }

        /**Metoda to String */
        @Override
        public String toString(){
            String s = new String();
            s+= name +": ";
            Car temp = first;
            do{
                
                if(temp.next.prev != temp){// sprawdzamy czy wagon nie jest na odwrot
                    temp.next.reverse();// jesli tak to obracamy
                }
                s+= temp.name + " ";
                temp = temp.next;
            } while(temp!=first);
            //s+=temp.name + " ";
            return s;
        }

        /**Wyswietla pociag na ekranie */
        public void display(){
            System.out.println(toString());
        }

        /**Sprawdza czy pociag ma wagony */
        public boolean isEmpty(){
            if(first==null) return true;
            return false;
        }

    }
    /***************************************************************************************/
    /*----------Pola klasy TrainList------------*/

    private Train trains;//referecja do pierwszego pociagu

    /*--------Metody---------*/
    
    /**Sprawdza czy nazwa jest dostepna */
    public  boolean isNameAvaiable(String name){ 
        Train temp = trains;
        while(temp!=null){
            if(temp.name.equals(name))
                return false;
            temp = temp.next;
        }
        return true; 
    };

    /**Konstuktor */
    public TrainList(){};

    /**Wstawia na poczatek listy  */
    public boolean pushFirst(String name,String carName){
        if(isNameAvaiable(name)){
            Train temp =new Train(name,carName);
            temp.next = trains;
            trains = temp;
            return true;
        }
        System.out.println("Train "+ name+" already exists");
        return false;
    }

    /**Wstawia na koniec.. Nie przydatne */
    public boolean pushLast(String name,String carName){
        if(isNameAvaiable(name)){
            Train temp = trains;
            if(temp==null){
                trains = new Train(name,carName);
                return true;
            }
            while(temp.next!=null){
                temp = temp.next;
            }
            
            temp.next = new Train(name,carName);
            return true;
        }
        System.out.println("Train "+ name +" already exist");
        return false;
    }
    /**Metoda toString */
    @Override
    public String toString(){
        String s = "Trains: ";
        Train temp = trains;
        while(temp!=null){
            s+=temp.name+" ";
            temp = temp.next;
        }
        return s;
    }

    /**Pokazuje */
    public void display(){
        System.out.println(toString());
    }

    /**Wyszukuje pociag w liscie */
    public Train search(String n){
        Train temp = trains;
        while(temp!= null){
            if(temp.name.equals(n)) return temp;
            temp = temp.next;
        }
        System.out.println("Train "+n+" does not exist");
        return null;
    }

    /**Usuwa pociag z listy */
    public void delate(String name){
        Train temp = search(name);
        if(temp == null) return;
        Train temp2 = trains;

        if(temp==trains) {
            trains = trains.next;
            return;
        }
        
        while(temp2.next!=temp){
            temp2=temp2.next;
        }
        temp2.next = temp2.next.next;
        
    }
}

/**Klasa glowna programu */
public class Source{
    static public Scanner sc = new Scanner(System.in);


    /*****************Program****************/
    static public void main(String[] args){
        int iloscPolecen , iloscZestawow = sc.nextInt(); // ilosc zestaow
        String polecenie;//na polecenie
        String[] polecenia;
        for(int j = 0;j<iloscZestawow; j++){
            TrainList trainList = new TrainList();
            iloscPolecen = sc.nextInt();// pobieramy ilosc polecen

            for(int i = 0; i <= iloscPolecen; i++){//tutaj wykonujemy polecniea

                polecenie = sc.nextLine();
                polecenia = polecenie.split(" ");

                switch (polecenia[0]){
                    case"New":{
                        trainList.pushFirst(polecenia[1], polecenia[2]);
                    }break;
                    case "InsertFirst":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        if(temp!= null) temp.pushFirst(polecenia[2]);
                    }break;
                    case "InsertLast":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        if(temp!= null) temp.pushLast(polecenia[2]);
                    }break;
                    case "Display":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        if(temp!= null) temp.display();
                    }break;
                    case "Trains":{
                        trainList.display();
                    }break;
                    case "Reverse":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        if(temp!= null) temp.reverseCars();
                    }break;
                    case "Union":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        TrainList.Train temp2 = trainList.search(polecenia[2]);
                        if(temp!= null && temp2!=null) temp.merge(temp2);
                        trainList.delate(polecenia[2]);
                    }break;
                    case "DelFirst":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        if(temp!= null) {
                            if(trainList.isNameAvaiable(polecenia[2])){
                                trainList.pushFirst(polecenia[2], temp.delateFirst());
                                if(temp.isEmpty()) trainList.delate(polecenia[1]);
                            }
                            else System.out.println("Train " + polecenia[2] + " already exists");
                        }else System.out.println("Train " + polecenia[1] + " does not exist");
                    }break;
                    case "DelLast":{
                        TrainList.Train temp = trainList.search(polecenia[1]);
                        if(temp!= null) {
                            if(trainList.isNameAvaiable(polecenia[2])){
                                trainList.pushFirst(polecenia[2], temp.delateLast());
                                if(temp.isEmpty()) trainList.delate(polecenia[1]);
                            }
                            else System.out.println("Train " + polecenia[2] + " already exists");
                        }else System.out.println("Train " + polecenia[1] + " does not exist");
                    }break;
                }

            }//koniec petli polecen

        }//tu sie konczy petla zestawow
    }//tu sie koncy main
}