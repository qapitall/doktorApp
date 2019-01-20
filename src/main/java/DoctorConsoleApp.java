import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoctorConsoleApp extends JFrame implements ActionListener {

    boolean flag = true;
    private JButton sendButton;
    private DatabaseReference ref;
    private JTextField diagnosisTextField;
    private JTextField locationTextField;
    private JLabel diagnosisLabel, locationLabel;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> diagnosis = new ArrayList<>();
    private Map<String,ArrayList<Map<String,ArrayList<String>>>> map = new HashMap<>();

    public DoctorConsoleApp(String city,String country) {
        
        //Firebase Admin App bağlantısı kuruldu
        
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("esalgin-967bee205ed8.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://esalgin.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Firebase de "Turkey" den baslayarak, önceki sayfada seçilmiş olan "City" ve "Country" gittik
        ref = FirebaseDatabase.getInstance()
                .getReference("Turkey").child(city).child(country);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //"Turkey-Ankara-Kecioren" düğümünü tüm çocuklarını çekip map a işledik,yani bir yerde tuttuk
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    locations.add(ds.getKey()+", "+country+", "+city);
                    ArrayList<Map<String,ArrayList<String>>> test = new ArrayList<>();
                    for(DataSnapshot ds2:ds.child("Diseases").getChildren()){
                        if(flag)
                        diagnosis.add(ds2.getKey());
                        ArrayList<String> size = new ArrayList<>();
                        for (DataSnapshot ds3 : ds2.getChildren()){
                            size.add(ds3.getValue().toString());
                        }
                        Map<String,ArrayList<String>> map = new HashMap<>();
                        map.put(ds2.getKey(),size);
                        test.add(map);
                    }
                    flag = false;
                    map.put(ds.getKey(),test);
                }

                //Sunucudan alınan verilerin işlenip map alınması bittikten sonra uygulamayı kullanıma açtık
                new Thread() {
                    public void run() {

                        while (true) {
                            if (diagnosis.size() > 1) {
                                locationTextField.setEditable(true);
                                diagnosisTextField.setEditable(true);
                                sendButton.setEnabled(true);
                            }
                        }
                    }
                }.start();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        diagnosisTextField = new JTextField(15);
        locationTextField = new JTextField(15);
        diagnosisLabel = new JLabel("Hastanın tanısı  :");
        locationLabel = new JLabel("Hastanın adresi :");
        sendButton = new JButton("Yolla");

        locationTextField.setEditable(false);
        diagnosisTextField.setEditable(false);
        sendButton.setEnabled(false);

        //Autocomplate TextField ilgili verilere göre ayarlandı
        AutoSuggestor autoSuggestor = new AutoSuggestor(locationTextField, this, null, Color.WHITE.brighter(), Color.BLUE, Color.RED, 1f) {
            @Override
            boolean wordTyped(String typedWord) {

                setDictionary(locations);

                return super.wordTyped(typedWord);
            }
        };
        autoSuggestor = new AutoSuggestor(diagnosisTextField, this, null, Color.WHITE.brighter(), Color.BLUE, Color.RED, 1f) {
            @Override
            boolean wordTyped(String typedWord) {

                setDictionary(diagnosis);

                return super.wordTyped(typedWord);
            }
        };

        Container container = getContentPane();
        container.setLayout(new FlowLayout());
        container.add(locationLabel);
        container.add(locationTextField);
        container.add(diagnosisLabel);
        container.add(diagnosisTextField);
        container.add(sendButton);

        sendButton.addActionListener(this);

    }

    //Sunucuya hastanın adresine göre hastalık türü bilgisi işlendi
    @Override
    public void actionPerformed(ActionEvent e) {

        //Sistemin gününe göre veriyi ilgili yere yerleştirilmesi

        if(sendButton == e.getSource()){
        Date simdikiZaman = new Date();
        DateFormat df = new SimpleDateFormat("dd");
        int day = Integer.parseInt(df.format(simdikiZaman))%15;
        String[] konum = locationTextField.getText().split(", ");
            int i=0;
            for(i=0; i< diagnosis.size(); i++){
                if(diagnosis.get(i).contains(diagnosisTextField.getText().trim()))
                    break;
            }

            int x = Integer.parseInt(map.get(konum[0]).get(i).get(diagnosisTextField.getText().trim()).get(day-1))+1;
            map.get(konum[0]).get(i).get(diagnosisTextField.getText().trim()).set(day-1,x+"");
            ref.child(konum[0]).child("Diseases").child(diagnosisTextField.getText().trim()).child(String.valueOf(day)).setValue(x,null);
    }
    }

}