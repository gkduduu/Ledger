package utils.jhy.ledger.data;

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

import java.util.HashMap;
import java.util.Map;

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
                                    assembleCategory(document.getData().toString(), that);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private static void assembleCategory(String data, InputDialog that) {
        data = data.trim();
        data = data.replace("{", "");
        data = data.replace("}", "");
        data = data.replace("=", "");
        that.setCategory(data.split(","));
    }

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
}
