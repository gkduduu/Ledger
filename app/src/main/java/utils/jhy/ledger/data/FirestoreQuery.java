package utils.jhy.ledger.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.jhy.ledger.BaseActivity;
import utils.jhy.ledger.dialog.InputDialog;

/**
 * Created by hayoung on 2019-08-27.
 * gkduduu@naver.com
 */
public class FirestoreQuery {

    final static String TAG = "jhy";

    public static void getConstData(final String what, final InputDialog that) {
        final Map<String, Object>[] result = new Map[1];

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Const.FIRESTORE_CONST)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().toString());
                                if (what.equals(document.getId())) {
                                    assembleConst(document.getData().toString(), that);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //카테고리 데이터 파싱
    private static void assembleConst(String data, InputDialog that) {
        data = data.trim();
        data = data.replace(" ","");
        data = data.replace("{", "");
        data = data.replace("}", "");
        data = data.replace("=", "");
        that.setCategory(data.split(","));
    }

    //가계 데이터 삽입
    public static void insertData(MainData data, final InputDialog that) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("state", data.getState());
        user.put("date", data.getDate());
        user.put("store", data.getStore());
        user.put("money", data.getMoney());
        user.put("comment", data.getComment());
        user.put("card", data.getCard());
        user.put("user", data.getUser());
        user.put("category", data.getCategory());

        db.collection(data.getDate().split("-")[0] + "년").document(data.getDate().split("-")[1] + "월").collection("리스트")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        that.dialogFinish(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        that.dialogFinish(false);
                    }
                });
    }

    //가계데이터 조회
    public static void getData(final BaseActivity act) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("2019년").document("08월").collection("리스트")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<MainData> data = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().toString());
                                MainData d = new MainData();
                                d.setComment(document.getData().toString());
                                data.add(d);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        act.callbackGetData(act, data);
                    }
                });
    }

}
