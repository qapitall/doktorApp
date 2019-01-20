import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectCity extends JFrame implements ActionListener {

    // Veri seçilmesi için eklendi

    private String[] citiesOfTheTurkey = { "Adana", "Adiyaman", "Afyon", "Ari", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydin", "Balikesir", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa", "Canakkale",
            "Cankiri", "Corum", "Denizli", "Diyarbakir", "Edirne", "Elazig", "Erzincan", "Erzurum", "Eskisehir",
            "Gaziantep", "Giresun", "Gumushane", "Hakkari", "Hatay", "Isparta", "Mersin", "Istanbul", "Izmir",
            "Kars", "Kastamonu", "Kayseri", "Kirklareli", "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya",
            "Manisa", "Kahramanmaras", "Mardin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Sanliurfa", "Usak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kirikkale", "Batman", "Sirnak",
            "Bartin", "Ardahan", "Igdir", "Yalova", "Karabuk", "Kilis", "Osmaniye", "Duzce"};

    private String[] townsOfTheAnkara =  {"Akyurt", "Altindag", "Ayas", "Balâ", "Beypazari", "Camlidere", "Cankaya", "Cubuk"
            , "Elmadag", "Etimesgut", "Evren", "Golbasi", "Gudul", "Haymana", "Kalecik", "Kahraman Kazan", "Kecioren"
            , "Kizilcahamam", "Mamak", "Nallihan", "Polatli", "Pursaklar", "Sincan", "Sereflikochisar", "Yenimahalle"};

    private JButton sendButton;
    private JComboBox citiesComboBox, townsComboBox;

    // Kullanıcı arayüzü
    public SelectCity(){

        Container container = getContentPane();
        container.setLayout(new FlowLayout());

        citiesComboBox = new JComboBox(citiesOfTheTurkey);
        citiesComboBox.setSelectedIndex(5);
        citiesComboBox.addActionListener(this);

        townsComboBox = new JComboBox(townsOfTheAnkara);
        townsComboBox.setSelectedIndex(16);
        townsComboBox.addActionListener(this);

        sendButton = new JButton("Sec");
        sendButton.addActionListener(this);

        container.add(citiesComboBox);
        container.add(townsComboBox);
        container.add(sendButton);

    }

    public static void main(String[] args){

        SelectCity selectCity = new SelectCity();
        selectCity.setVisible(true);
        selectCity.setResizable(false);
        selectCity.setSize(300, 120);
        selectCity.setTitle("Salgin~Doktor Arayuzu");
        selectCity.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        // Listede verisi olmayan bir şey seçilirse

        if(e.getSource() == sendButton){
            if(townsComboBox.getSelectedItem().toString().equals("Henüz veri eklenmedi"))
                return; // Çıkış

            //Seçilmiş olan veriyle ilgili sayfa başlatıldı

            DoctorConsoleApp doctorConsoleApp = new DoctorConsoleApp(citiesComboBox.getSelectedItem().toString(), townsComboBox.getSelectedItem().toString());
            doctorConsoleApp.setVisible(true);
            doctorConsoleApp.setResizable(false);
            doctorConsoleApp.setSize(300, 120);
            doctorConsoleApp.setTitle("Salgin~Doktor Arayuzu");
            doctorConsoleApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setVisible(false);

        }
        else if(e.getSource() == citiesComboBox){

            // Ankara dışında veri olmadığı için bu kontrol edildi
            if(citiesComboBox.getSelectedIndex()!=5){
                townsComboBox.removeAllItems();
                townsComboBox.addItem("Henuz veri eklenmedi");
            }
            else{
                townsComboBox.removeAllItems();
                for(int i = 0; i< townsOfTheAnkara.length; i++)
                townsComboBox.addItem(townsOfTheAnkara[i]);
            }

       }
    }
}
