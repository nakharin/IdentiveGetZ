package identive.usb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.identive.libs.SCard;
import com.identive.libs.SCard.SCARD_READERSTATE;
import com.identive.libs.SCard.SCardAttribute;
import com.identive.libs.SCard.SCardIOBuffer;
import com.identive.libs.SCard.SCardState;
import com.identive.libs.WinDefs;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class IdentiveGetZActivity extends Activity {

    public static String selectedRdr = null;
    public static String atr = null;
    private static CharSequence[] items = null;
    private static final int PICKFILE_RESULT_CODE = 1;
    private static String cmd[] = null;
    private static String value[] = null;
    private TextView command = null;
    private TextView result = null;
    private static int reqCount = 1;
    Button selectFile = null;
    Button nextCmd = null;
    Button runScenario = null;
    Button cTransmit = null;
    Button getStatus = null;
    Button ControlExecute = null;
    Button cConnect = null;
    Button cReConnect = null;
    Button cDisConnect = null;
    Button SelectReader = null;
    Spinner spin = null;
    Button btnGetAttrib = null;
    int selectedProtocol;
    int selectedDisposition;
    int selectedMode;
    int nItemSelected;
    int bAttrFlag = 0;
    private String sstr = "";
    boolean flag = true;
    static int logcount = 0;
    Context mContext = null;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        spin = (Spinner) findViewById(R.id.spinner1);
        mContext = IdentiveGetZActivity.this;
        btnGetAttrib = (Button) findViewById(R.id.Button06);
        getStatus = (Button) findViewById(R.id.button07);
        ControlExecute = (Button) findViewById(R.id.button08);
        SelectReader = (Button) findViewById(R.id.button09);
        cConnect = (Button) findViewById(R.id.button10);
        cReConnect = (Button) findViewById(R.id.button11);
        cDisConnect = (Button) findViewById(R.id.button12);
        selectFile = (Button) findViewById(R.id.button13);
        nextCmd = (Button) findViewById(R.id.button14);
        runScenario = (Button) findViewById(R.id.button15);
        cTransmit = (Button) findViewById(R.id.button16);
        command = (TextView) findViewById(R.id.editText2);
        result = (TextView) findViewById(R.id.editText4);
        cConnect.setEnabled(false);
        cReConnect.setEnabled(false);
        cDisConnect.setEnabled(false);
        nextCmd.setEnabled(false);
        selectFile.setEnabled(false);
        cTransmit.setEnabled(false);
        runScenario.setEnabled(false);
        ControlExecute.setEnabled(false);
        getStatus.setEnabled(false);
        spin.setEnabled(false);
        btnGetAttrib.setEnabled(false);

        SCard trans = new SCard();
        long lRetval = trans.USBRequestPermission(getApplicationContext());
        lRetval = trans.SCardEstablishContext(getBaseContext());
        Log.d("IdenitveGetZ", "Result - " + lRetval);

        final String[] AttribList = getResources().getStringArray(R.array.ScardGetAttribList);

        ArrayAdapter<String> localAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AttribList);
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(localAdapter);

        spin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                switch (pos) {
                    case 0:
                        nItemSelected = WinDefs.SCARD_ATTR_VENDOR_NAME;
                        break;
                    case 1:
                        nItemSelected = WinDefs.SCARD_ATTR_VENDOR_IFD_TYPE;
                        break;
                    case 2:
                        nItemSelected = WinDefs.SCARD_ATTR_VENDOR_IFD_VERSION;
                        bAttrFlag = 1;
                        break;
                    case 3:
                        nItemSelected = WinDefs.SCARD_ATTR_VENDOR_IFD_SERIAL_NO;
                        break;
                    case 4:
                        nItemSelected = WinDefs.SCARD_ATTR_CHANNEL_ID;
                        bAttrFlag = 1;
                        break;
                    case 5:
                        nItemSelected = WinDefs.SCARD_ATTR_PROTOCOL_TYPES;
                        bAttrFlag = 1;
                        break;
                    case 6:
                        nItemSelected = WinDefs.SCARD_ATTR_DEFAULT_CLK;
                        bAttrFlag = 2;
                        break;
                    case 7:
                        nItemSelected = WinDefs.SCARD_ATTR_MAX_CLK;
                        bAttrFlag = 2;
                        break;
                    case 8:
                        nItemSelected = WinDefs.SCARD_ATTR_DEFAULT_DATA_RATE;
                        bAttrFlag = 2;
                        break;
                    case 9:
                        nItemSelected = WinDefs.SCARD_ATTR_MAX_DATA_RATE;
                        bAttrFlag = 2;
                        break;
                    case 10:
                        nItemSelected = WinDefs.SCARD_ATTR_MAX_IFSD;
                        bAttrFlag = 2;
                        break;
                    case 11:
                        nItemSelected = WinDefs.SCARD_ATTR_POWER_MGMT_SUPPORT;
                        bAttrFlag = 2;
                        break;
                    case 12:
                        nItemSelected = WinDefs.SCARD_ATTR_CHARACTERISTICS;
                        bAttrFlag = 1;
                        break;
                    case 13:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_PROTOCOL_TYPE;
                        bAttrFlag = 1;
                        break;
                    case 14:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_CLK;
                        bAttrFlag = 2;
                        break;
                    case 15:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_F;
                        bAttrFlag = 2;
                        break;
                    case 16:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_D;
                        bAttrFlag = 2;
                        break;
                    case 17:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_N;
                        bAttrFlag = 2;
                        break;
                    case 18:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_W;
                        bAttrFlag = 2;
                        break;
                    case 19:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_IFSC;
                        bAttrFlag = 2;
                        break;
                    case 20:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_IFSD;
                        bAttrFlag = 2;
                        break;
                    case 21:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_BWT;
                        bAttrFlag = 2;
                        break;
                    case 22:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_CWT;
                        bAttrFlag = 2;
                        break;
                    case 23:
                        nItemSelected = WinDefs.SCARD_ATTR_CURRENT_EBC_ENCODING;
                        bAttrFlag = 2;
                        break;
                    case 24:
                        nItemSelected = WinDefs.SCARD_ATTR_ICC_PRESENCE;
                        bAttrFlag = 1;
                        break;
                    case 25:
                        nItemSelected = WinDefs.SCARD_ATTR_ICC_INTERFACE_STATUS;
                        bAttrFlag = 1;
                        break;
                    case 26:
                        nItemSelected = WinDefs.SCARD_ATTR_ATR_STRING;
                        bAttrFlag = 1;
                        break;
                    case 27:
                        nItemSelected = WinDefs.SCARD_ATTR_ICC_TYPE_PER_ATR;
                        bAttrFlag = 1;
                        break;
                    case 28:
                        nItemSelected = WinDefs.SCARD_ATTR_DEVICE_UNIT;
                        break;
                    case 29:
                        nItemSelected = WinDefs.SCARD_ATTR_DEVICE_IN_USE;
                        break;
                    case 30:
                        nItemSelected = WinDefs.SCARD_ATTR_DEVICE_FRIENDLY_NAME;
                        break;
                    case 31:
                        nItemSelected = WinDefs.SCARD_ATTR_DEVICE_SYSTEM_NAME;
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }


    private class Update extends AsyncTask<Void, String, Void> {

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            result.setText(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            SCard trans = new SCard();
            SCARD_READERSTATE[] rgReaderStates = new SCARD_READERSTATE[5];
            rgReaderStates[0] = trans.new SCARD_READERSTATE();
            rgReaderStates[0].setnCurrentState(WinDefs.SCARD_STATE_UNAWARE);
            rgReaderStates[0].setSzReader(selectedRdr);
            sstr = "";
            do {
                if (flag) break;
                long lRetVal = trans.SCardGetStatusChange(0, rgReaderStates, 1);
                if ((rgReaderStates[0].getnEventState() & WinDefs.SCARD_STATE_CHANGED) == WinDefs.SCARD_STATE_CHANGED) {
                    rgReaderStates[0].setnEventState(rgReaderStates[0].getnEventState() - WinDefs.SCARD_STATE_CHANGED);
                    if (rgReaderStates[0].getnEventState() == WinDefs.SCARD_STATE_PRESENT) {
                        sstr = "";
                        for (int i = 0; i < rgReaderStates[0].getnAtr(); i++) {
                            int temp = rgReaderStates[0].getabyAtr()[i] & 0xFF;
                            if (temp < 16) {
                                sstr = sstr.toUpperCase() + "0" + Integer.toHexString(rgReaderStates[0].getabyAtr()[i]) + " ";
                            } else {
                                sstr = sstr.toUpperCase() + Integer.toHexString(temp) + " ";
                            }
                        }
                    } else {
                        sstr = "Card Absent";
                    }
//				}else{
//					sstr = "State not changed";
                }
                publishProgress(sstr);
                int nTemp = rgReaderStates[0].getnCurrentState();
                rgReaderStates[0].setnCurrentState(rgReaderStates[0].getnEventState());
                rgReaderStates[0].setnEventState(nTemp);

            } while (true);

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            getStatus.setText("Start Tracking");
            if (!cConnect.isEnabled()) {
                if (selectedMode != 3) {
                    selectFile.setEnabled(true);
                    cTransmit.setEnabled(true);
                }
                cReConnect.setEnabled(true);
                cDisConnect.setEnabled(true);
                ControlExecute.setEnabled(true);
                spin.setEnabled(true);
                btnGetAttrib.setEnabled(true);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClickSetAttrib(View v) {
        long lRetval = WinDefs.SCARD_S_SUCCESS;
        String rstr = "";
        Long length = new Long(0);
        do {
            SCard trans = new SCard();
            SCardAttribute attrib = trans.new SCardAttribute();
            attrib.setnAttrId(nItemSelected);
            lRetval = trans.SCardGetAttrib(attrib);
            if (lRetval == WinDefs.SCARD_S_SUCCESS) {
                if (bAttrFlag != 1) {
                    if (attrib.getnAttrLen() == 4) {
                        for (int i = 0; i < 4; i++) {
                            length += ((long) attrib.getAbyAttr()[i] & 0xff) << (8 * i);
                        }
                        result.setText(length.toString());
                    } else {
                        rstr = new String(attrib.getAbyAttr());
                        result.setText(rstr);
                    }
                } else {
                    for (int i = attrib.getnAttrLen() - 1; i >= 0; i--) {
                        int temp = attrib.getAbyAttr()[i] & 0xFF;
                        if (temp < 16) {
                            rstr = rstr.toUpperCase() + "0" + Integer.toHexString(attrib.getAbyAttr()[i]);
                        } else {
                            rstr = rstr.toUpperCase() + Integer.toHexString(temp);
                        }
                    }
                    result.setText(rstr);
                }
            }
        } while (false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        cmd = new String[500];
        value = new String[500];
        int strlen = 0;
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    try {
                        FileInputStream fstream = new FileInputStream(FilePath);
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String temp;

                        int i = 1;
                        //Read File Line By Line
                        while ((temp = br.readLine()) != null) {
                            // Print the content on the console
                            if (temp.toCharArray().length != 0) {
                                char[] ch = temp.toCharArray();
                                if (ch[0] == '#' || ch[0] == '!') {
                                    continue;
                                } else if (ch[0] == '2' && ch[2] == '1') {
                                    cmd[0] = "0";
                                    temp = br.readLine();
                                    ch = temp.toCharArray();
                                    strlen = ch.length - 1;
                                    char[] ch1 = new char[ch.length - 1];
                                    System.arraycopy(ch, 1, ch1, 0, (ch.length - 1));
                                    value[0] = new String(ch1);
                                } else if (ch[0] == '>') {
                                    char[] ch1 = new char[ch.length - 1];
                                    System.arraycopy(ch, 1, ch1, 0, (ch.length - 1));
                                    value[i] = new String(ch1);
                                    i++;
                                } else if (ch[0] == '2') {
                                    char[] ch1 = new char[ch.length - 4];
                                    System.arraycopy(ch, 4, ch1, 0, (ch.length - 4));
                                    cmd[i] = new String(ch1);
                                } else {
                                    cmd[i] = temp;
                                }
                            }
                        }
                        //Close the input stream
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        if (value[0] != null) {
            String str = value[0].substring(0, strlen - 4).toLowerCase();
            if (str.equals(atr)) {
                selectFile.setEnabled(false);
                nextCmd.setEnabled(true);
                cTransmit.setEnabled(true);
                runScenario.setEnabled(true);
                command = (TextView) findViewById(R.id.editText2);
                command.setText(cmd[1]);
            } else {
                Toast.makeText(getBaseContext(), "The Req File do not match", Toast.LENGTH_SHORT).show();
                selectFile.setEnabled(true);
                cmd = null;
                value = null;
                result.setText("");    //B
            }
        } else {
            selectFile.setEnabled(false);
            nextCmd.setEnabled(true);
            cTransmit.setEnabled(true);
            runScenario.setEnabled(true);
            command = (TextView) findViewById(R.id.editText2);
            command.setText(cmd[1]);
            result.setText("");        //B
        }
    }

    public void onCLickSelectReader(View v) {
        long lRetval = 0;
        ArrayList<String> deviceList = new ArrayList<String>();
        SCard trans = new SCard();
        lRetval = trans.SCardListReaders(getBaseContext(), deviceList);
        Log.d("IdenitveGetZ", "Result - " + lRetval);
        items = deviceList.toArray(new CharSequence[deviceList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select a Reader");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selectedRdr = (String) items[item];
                Log.d("IdenitveGetZ", "Selected Reader - " + selectedRdr);
                dialog.dismiss();
                cConnect.setEnabled(true);
                getStatus.setEnabled(true);
                SelectReader.setEnabled(false);
                TextView rdrName = (TextView) findViewById(R.id.editText1);
                rdrName.setText(selectedRdr);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public void onCLickCardConnect(View v) {
        final Dialog dlg = new Dialog(mContext);
        dlg.setTitle("Reset Parameters");

        dlg.setContentView(R.layout.connect);

        final ListView mode = (ListView) dlg.findViewById(R.id.listView1);
        String[] tempList = getResources().getStringArray(R.array.SCardModeList);
        ArrayAdapter<String> localAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_single_choice, tempList);
        mode.setAdapter(localAdapter);

        final ListView protocol = (ListView) dlg.findViewById(R.id.listView2);
        tempList = getResources().getStringArray(R.array.ScardProtocolList);
        localAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_single_choice, tempList);
        protocol.setAdapter(localAdapter);

        Button ok = (Button) dlg.findViewById(R.id.button1);
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String sstr = "";
                dlg.dismiss();
                do {

                    selectedMode = mode.getCheckedItemPosition() + 1;
                    if (selectedMode == 3) {
                        selectedProtocol = 0;
                    } else {
                        selectedProtocol = protocol.getCheckedItemPosition() + 1;
                    }

                    SCard trans = new SCard();
                    atr = new String();
                    long status = trans.SCardConnect(selectedRdr, selectedMode, selectedProtocol);
                    if (status != 0) {
                        SelectReader.setEnabled(true);
                        Toast.makeText(getBaseContext(), "Card Connection Error", Toast.LENGTH_SHORT).show();
                        result.setText("");
                    } else {
                        SCardState cardState = trans.new SCardState();
                        status = trans.SCardStatus(cardState);
                        for (int i = 0; i < cardState.getnATRlen(); i++) {
                            int temp = cardState.getAbyATR()[i] & 0xFF;
                            if (temp < 16) {
                                atr = atr + "0" + Integer.toHexString(cardState.getAbyATR()[i]);
                                sstr = sstr + "0" + Integer.toHexString(cardState.getAbyATR()[i]) + " ";
                            } else {
                                atr = atr + Integer.toHexString(temp);
                                sstr = sstr + Integer.toHexString(temp) + " ";
                            }
                        }
                        result.setText(sstr);
                        Toast.makeText(getBaseContext(), "Card Connected Successfully", Toast.LENGTH_SHORT).show();
                        if (selectedMode != 3) {
                            selectFile.setEnabled(true);
                            cTransmit.setEnabled(true);
                        }
                        cReConnect.setEnabled(true);
                        cConnect.setEnabled(false);
                        cDisConnect.setEnabled(true);
                        ControlExecute.setEnabled(true);
                        getStatus.setEnabled(true);
                        spin.setEnabled(true);
                        btnGetAttrib.setEnabled(true);
                    }
                } while (false);

            }
        });
        dlg.show();
    }

    public void onCLickCardReConnect(View v) {
        final Dialog dlg = new Dialog(mContext);
        dlg.setTitle("Reset Parameters");
        dlg.setContentView(R.layout.reconnect);

        final ListView initialization = (ListView) dlg.findViewById(R.id.listView1);
        String[] tempList = new String[]{"Leave Card", "Reset Card", "Unpower Card"};
        ArrayAdapter<String> localAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_single_choice, tempList);
        initialization.setAdapter(localAdapter);

        final ListView protocol = (ListView) dlg.findViewById(R.id.listView2);
        tempList = getResources().getStringArray(R.array.ScardProtocolList);
        localAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_single_choice, tempList);
        protocol.setAdapter(localAdapter);

        final ListView mode = (ListView) dlg.findViewById(R.id.listView3);
        tempList = new String[]{"Exclusive Mode", "Shared Mode"};
        localAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_single_choice, tempList);
        mode.setAdapter(localAdapter);

        Button ok = (Button) dlg.findViewById(R.id.button1);
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String sstr = "";
                dlg.dismiss();
                do {

                    selectedDisposition = initialization.getCheckedItemPosition();
                    selectedProtocol = protocol.getCheckedItemPosition() + 1;
                    selectedMode = mode.getCheckedItemPosition() + 1;
                    SCard trans = new SCard();
                    atr = new String();
                    long status = trans.SCardReconnect(selectedMode, selectedProtocol, selectedDisposition);
                    if (status != 0) {
                        Toast.makeText(getBaseContext(), "Card Connection Error", Toast.LENGTH_SHORT).show();
                        result.setText("");
                        cTransmit.setEnabled(false);
                        selectFile.setEnabled(false);
                        cReConnect.setEnabled(false);
                        cDisConnect.setEnabled(false);
                        cConnect.setEnabled(true);
                        ControlExecute.setEnabled(false);
                    } else {
                        SCardState cardState = trans.new SCardState();
                        status = trans.SCardStatus(cardState);
                        for (int i = 0; i < cardState.getnATRlen(); i++) {
                            int temp = cardState.getAbyATR()[i] & 0xFF;
                            if (temp < 16) {
                                atr = atr + "0" + Integer.toHexString(cardState.getAbyATR()[i]);
                                sstr = sstr + "0" + Integer.toHexString(cardState.getAbyATR()[i]) + " ";
                            } else {
                                atr = atr + Integer.toHexString(temp);
                                sstr = sstr + Integer.toHexString(temp) + " ";
                            }
                        }
                        result.setText(sstr);
                        if (selectedMode != 3) {
                            selectFile.setEnabled(true);
                            nextCmd.setEnabled(false);
                            cTransmit.setEnabled(true);
                            runScenario.setEnabled(false);

                        }
                        SelectReader.setEnabled(false);
                        ControlExecute.setEnabled(true);
                        getStatus.setEnabled(true);
                        spin.setEnabled(true);
                        btnGetAttrib.setEnabled(true);
                        command.setText("");
                        reqCount = 1;
                    }
                } while (false);
            }
        });

        dlg.show();
    }

    public void onCLickCardDisConnect(View v) {
        String[] ResetStatus = getResources().getStringArray(R.array.ScardInitializationList);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Reset Status");
        builder.setSingleChoiceItems(ResetStatus, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selectedDisposition = item + 1;
                dialog.dismiss();
                SCard trans = new SCard();
                trans.SCardDisconnect(selectedDisposition);    //(WinDefs.SCARD_UNPOWER_CARD)
                cConnect.setEnabled(true);
                cReConnect.setEnabled(false);
                cDisConnect.setEnabled(false);
                nextCmd.setEnabled(false);
                selectFile.setEnabled(false);
                ControlExecute.setEnabled(false);
                cTransmit.setEnabled(false);
                runScenario.setEnabled(false);
                getStatus.setEnabled(true);
                spin.setEnabled(false);
                btnGetAttrib.setEnabled(false);
                SelectReader.setEnabled(true);
                command.setText("");
                result.setText("");
                reqCount = 1;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onCLickSelectReq(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    public void onCLickNextCommand(View v) {
        reqCount++;
        if (cmd[reqCount] != null) {
            command.setText(cmd[reqCount]);

        } else {
            reqCount = 1;
            command.setText(cmd[reqCount]);
            selectFile.setEnabled(true);
        }
    }

    public void onCLickRunScenario(View v) {
        long[] time = new long[cmd.length];
        String rstr = "";
        int nfail = 0, i = 0;
        int sum = 0;
        FileOutputStream fos = null;
        SCard trans = new SCard();
        try {
            boolean flag = false;
            File mediaDir = new File("/sdcard/IdentiveGetZ");
            if (!mediaDir.exists()) {
                mediaDir.mkdir();
            }
            File file = new File("/sdcard/IdentiveGetZ/log" + logcount + ".txt");
            logcount++;
            file.createNewFile();
            fos = new FileOutputStream(file);
            reqCount = 1;
            for (i = 1; cmd[i] != null; i++) {
                flag = false;
                String transmitCmd = cmd[i];
                byte[] buf = transmitCmd.getBytes();
                byte[] inbuf = new byte[transmitCmd.length() / 2];
                for (int j = 0; j < (inbuf.length * 2); j++) {
                    if (48 <= buf[j] && buf[j] <= 57) {
                        buf[j] = (byte) ((buf[j] & 0x0F));
                    } else if ((97 <= buf[j] && buf[j] <= 102) || (65 <= buf[j] && buf[j] <= 70)) {
                        buf[j] = (byte) ((buf[j] + 9) & 0x0F);
                    }
                }
                for (int k = 0, j = 0; k < inbuf.length * 2; k++, j++) {
                    inbuf[j] = (byte) ((buf[k] << 4) | (buf[++k]));
                }


                SCardIOBuffer transmit = trans.new SCardIOBuffer();
                transmit.setnInBufferSize(inbuf.length);
                transmit.setAbyInBuffer(inbuf);
                transmit.setnOutBufferSize(0x8000);
                transmit.setAbyOutBuffer(new byte[0x8000]);
                long lStartTime = new Date().getTime(); //start time
                long lRetval = trans.SCardTransmit(transmit);
                long lEndTime = new Date().getTime(); //end time
                long difference = lEndTime - lStartTime; //check different
                time[reqCount] = difference;
                fos.write(("Time : " + time[reqCount] + "\n").getBytes());
                if (lRetval != 0)
                    break;

                for (int k = 0; k < transmit.getnBytesReturned(); k++) {
                    int temp = transmit.getAbyOutBuffer()[k] & 0xFF;
                    if (temp < 16) {
                        rstr = rstr.toUpperCase() + "0" + Integer.toHexString(transmit.getAbyOutBuffer()[k]);
                    } else {
                        rstr = rstr.toUpperCase() + Integer.toHexString(temp);
                    }
                }
                char charray1[] = rstr.toLowerCase().toCharArray();
                char charray2[] = value[reqCount].toLowerCase().toCharArray();
                transmitCmd = i + "\n" + "2,0 " + transmitCmd + "\n";
                fos.write(transmitCmd.getBytes());
                rstr = ">" + rstr + "\n";
                fos.write(rstr.getBytes());
                if (charray2.length != 0) {
                    if (charray1.length == charray2.length) {
                        for (int l = 0; l < charray1.length; l++) {
                            if (charray2[l] == 'x' || charray2[l] == 'X' || charray2[l] == charray1[l]) {
                                continue;
                            } else flag = true;
                        }
                        if (flag) {
                            fos.write("Compare Error\n".getBytes());
                            nfail++;
                        }
                    } else {
                        fos.write("Compare Error\n".getBytes());
                        nfail++;
                    }
                }
                fos.write("\n".getBytes());
                reqCount++;
                rstr = "";
            }

            for (int k = 1; k <= reqCount; k++) {
                sum += time[k];
            }
            nextCmd.setEnabled(false);
            cTransmit.setEnabled(true);
            runScenario.setEnabled(false);
            selectFile.setEnabled(true);
            reqCount = 1;
            Toast.makeText(getBaseContext(), "Run Scenario Completed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            rstr = "Total commands = " + (--i) + "\n";
            rstr = rstr + "Total Passed = " + (i - nfail) + "\n";
            rstr = rstr + "Total Failed = " + nfail + "\n";
            rstr = rstr + "Total Time = " + sum + "\n";
            try {
                fos.write(rstr.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String bytesArrayToHexString(byte[] bytes) {

        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void onClickGetAttrib(View v) {
        long lRetval = WinDefs.SCARD_S_SUCCESS;
        String rstr = "";
        do {
            SCard trans = new SCard();
            SCardAttribute attrib = trans.new SCardAttribute();

            Toast.makeText(this, "nItemSelected : " + nItemSelected, Toast.LENGTH_SHORT).show();

            attrib.setnAttrId(nItemSelected);
            lRetval = trans.SCardGetAttrib(attrib);
            if (lRetval != WinDefs.SCARD_S_SUCCESS) {
                Toast.makeText(mContext, "Parameter Not Supported", Toast.LENGTH_SHORT).show();
                break;
            }
            for (int i = 0; i < attrib.getnAttrLen(); i++) {
                int temp = attrib.getAbyAttr()[i] & 0xFF;
                if (temp < 16) {
                    rstr = rstr + "0" + Integer.toHexString(attrib.getAbyAttr()[i]).toUpperCase() + " ";
                } else {
                    rstr = rstr + Integer.toHexString(temp).toUpperCase() + " ";
                }
            }
            result.setText(rstr);
        } while (false);
    }

    public byte[] transmit(byte[] apdu) {

        SCard sc = new SCard();
        SCardIOBuffer io = sc.new SCardIOBuffer();
        io.setnInBufferSize(apdu.length);
        io.setAbyInBuffer(apdu);
        io.setnOutBufferSize(0x8000);
        io.setAbyOutBuffer(new byte[0x8000]);
        long lRetval = sc.SCardTransmit(io);

        resultCommand += "lRetval : " + lRetval + "\n";

        if (lRetval != 0) {
            return new byte[]{0x00};
        }

        return Arrays.copyOf(io.getAbyOutBuffer(), io.getnBytesReturned());
    }

    public byte[] transmitExtended(byte[] apdu) {
        byte[] recv = transmit(apdu);

        resultCommand += "recv : " + bytesArrayToHexString(recv) + "\n";

        byte sw1 = recv[recv.length - 2];
        byte sw2 = recv[recv.length - 1];
        recv = Arrays.copyOf(recv, recv.length - 2);
        if (sw1 == 0x61) {
            recv = concat(recv, transmit(new byte[]{0x00, (byte) 0xC0, 0x00, 0x00, sw2}));
        } else if (!(sw1 == (byte) 0x90 && sw2 == (byte) 0x00)) {
            Toast.makeText(this, "ScardOperationUnsuccessfulException(\"SW != 9000\")", Toast.LENGTH_SHORT).show();
        }
        return recv;
    }

    private static byte[] concat(byte[]... arrays) {
        int size = 0;
        for (byte[] array : arrays) {
            size += array.length;
        }
        byte[] result = new byte[size];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    private String GetUTF8FromAsciiBytes(byte[] ascii_bytes) {
        String ascii = null;
        try {
            ascii = new String(ascii_bytes, "TIS620");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "GetUTF8FromAsciiBytes", Toast.LENGTH_SHORT).show();
        }
        byte[] utf8 = null;
        try {
            assert ascii != null;
            utf8 = ascii.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "GetUTF8FromAsciiBytes", Toast.LENGTH_SHORT).show();
        }
        String result = null;
        try {
            assert utf8 != null;
            result = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "GetUTF8FromAsciiBytes", Toast.LENGTH_SHORT).show();
        }
        assert result != null;
        return result.substring(0, result.length() - 2);
    }

    /**
     * Step 1 : Click Button [SELECT READER] and select card device
     *
     * Step 2 : Click Button [CARD CONNECT] and Mode [Exclusive] and Protocol Type [Protocol Tx]
     *
     * Step 3 : Put command [Select] Thailand Personal DF Name 00a4040008a000000054480001 and Click TRANSMIT (if your change card your must renew Step 3 before Step 4)
     *
     * Step 4 : Put command [CID] 80b0000402000d
     *
     * Step 5 : retry Put command [XXX] in https://github.com/chakphanu/ThaiNationalIDCard/blob/master/APDU.md
     */

    String resultCommand = "";

    public void onCLickTransmit(View v) {

        resultCommand = "";

        String hex = command.getText().toString();

        resultCommand += "hex : " + hex + "\n";

        byte[] apdu = hexStringToByteArray(hex);

        resultCommand += "apdu : " + bytesArrayToHexString(apdu) + "\n";

        byte[] byteResult = transmitExtended(apdu);
        if (byteResult == null) {
            resultCommand += "Error : Failed to send apdu";
        } else {
            resultCommand += "Result-UTF8 : " + GetUTF8FromAsciiBytes(byteResult);
        }

        result.setText(resultCommand);
    }

    public void onCLickTracking(View v) {
        Update up = new Update();
        if (getStatus.getText().equals("Start Tracking")) {
            up.execute();
            flag = false;
            getStatus.setText("Stop Tracking");
            if (selectedMode != 3) {
                nextCmd.setEnabled(false);
                selectFile.setEnabled(false);
                cTransmit.setEnabled(false);
                runScenario.setEnabled(false);
            }
            cReConnect.setEnabled(false);
            cDisConnect.setEnabled(false);
            ControlExecute.setEnabled(false);
            spin.setEnabled(false);
            btnGetAttrib.setEnabled(false);
        } else {
            flag = true;
        }
    }

    public void onCLickSCardControl(View v) {
        String rstr = "", sstr = "";
        String transmitCmd = command.getText().toString();
        byte[] buf = transmitCmd.getBytes();
        byte[] inbuf = new byte[transmitCmd.length() / 2];
        do {
            for (int i = 0; i < inbuf.length * 2; i++) {
                if (48 <= buf[i] && buf[i] <= 57) {
                    buf[i] = (byte) ((buf[i] & 0x0F));
                } else if ((97 <= buf[i] && buf[i] <= 102) || (65 <= buf[i] && buf[i] <= 70)) {
                    buf[i] = (byte) ((buf[i] + 9) & 0x0F);
                }
            }
            for (int i = 0, j = 0; i < inbuf.length * 2; ++i, j++) {
                inbuf[j] = (byte) ((buf[i] << 4) | (buf[++i]));
            }

            SCard trans = new SCard();
            SCardIOBuffer transmit = trans.new SCardIOBuffer();
            transmit.setnInBufferSize(inbuf.length);
            transmit.setAbyInBuffer(inbuf);
            transmit.setnOutBufferSize(0x2000);
            transmit.setAbyOutBuffer(new byte[0x2000]);
            long lRetval = trans.SCardControl((int) WinDefs.IOCTL_CCID_ESCAPE, transmit);
            if (lRetval != 0) {
                Toast.makeText(getApplicationContext(), "Unkown Command", Toast.LENGTH_SHORT).show();
                break;
            }

            for (int i = 0; i < transmit.getnBytesReturned(); i++) {
                int temp = transmit.getAbyOutBuffer()[i] & 0xFF;
                if (temp < 16) {
                    rstr = rstr.toUpperCase() + "0" + Integer.toHexString(transmit.getAbyOutBuffer()[i]);
                    sstr = sstr.toUpperCase() + "0" + Integer.toHexString(transmit.getAbyOutBuffer()[i]) + " ";
                } else {
                    rstr = rstr.toUpperCase() + Integer.toHexString(temp);
                    sstr = sstr.toUpperCase() + Integer.toHexString(temp) + " ";
                }
            }
            result.setText(sstr);
        } while (false);
    }

    @Override
    protected void onDestroy() {
        SCard trans = new SCard();
        long lRetval = trans.SCardDisconnect(1);    //(WinDefs.SCARD_UNPOWER_CARD)
        lRetval = trans.SCardReleaseContext();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }


}