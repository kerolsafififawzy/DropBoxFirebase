package com.kerolsme.dropboxfirebase.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kerolsme.dropboxfirebase.Models.FileModel;
import com.kerolsme.dropboxfirebase.Models.UserModel;

import java.util.Objects;

public class CloudDatabase {

    private FirebaseUser mAuthUser;
    private DatabaseReference firebaseDatabase;
    private FirebaseStorage storageRef;
    private Context context;
    private StorageTask mUploadTask;
    private TheObserver theObserver;


    public CloudDatabase(Context context) {
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuthUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference(mAuthUser.getUid());
        storageRef = FirebaseStorage.getInstance();
        theObserver = new TheObserver(context);
    }


    public void CheckAndRegister(boolean isNew, CloudResult result) {
        FirebaseDatabase.getInstance().getReference(mAuthUser.getUid()).child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isNew || snapshot.getChildrenCount() == 0) {
                    SaveUser(result);
                } else {
                    ReadUserData(result);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + "LOGIN" , error.toException() );
            }
        });

    }


    private void SaveUser(CloudResult result) {
        firebaseDatabase.child("user").setValue(new UserModel(mAuthUser.getEmail(), mAuthUser.getUid(), mAuthUser.getDisplayName(),
                        Objects.requireNonNull(mAuthUser.getPhotoUrl()).toString(), false, 0L, 2147483648L))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                        theObserver.setLimitedSize(2147483648L);
                        theObserver.SaveSize(0L);
                        result.onSucceed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        result.onError(e);
                    }
                });
    }

    public void FileSave(Uri uriFile, CloudResultProgress result) {
        long FileSize = getRealSizeFromUri(uriFile);
        long size = FileSize + theObserver.getSize();

        if (size <= theObserver.getLimitedSize()) {

            String extension = getFileExtension(uriFile);
            StorageReference fileReference = storageRef.getReference(mAuthUser.getUid()).child(System.currentTimeMillis() + "." + extension);
            mUploadTask = fileReference.putFile(uriFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            theObserver.SaveSize(size);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    saveDataRealTime(extension, uri, uriFile, fileReference.getName(), result);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            result.onError(e);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            result.onProgress((int) progress);
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                        }
                    });
            result.StorageFirebase(mUploadTask);
        } else {
            result.onError(new Throwable("Size Limited"));
        }
    }

    public void Delete(String key, String data, CloudResult result) {

        StorageReference desertRef = storageRef.getReferenceFromUrl(data);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                firebaseDatabase.child("Files").child(key).setValue(null);
                result.onSucceed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                firebaseDatabase.child("Files").child(key).setValue(null);
                result.onError(exception);
            }
        });

    }

    public void RenameFile(String key ,String NewName ,CloudResult result) {
        firebaseDatabase.child("Files").orderByChild("name").equalTo(NewName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() <= 0){
                    StartRenameFile(key,NewName,result);
                }else {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result.onError(error.toException());
            }
        });


    }
    public void RenameUser (String newName , CloudResult result) {

        firebaseDatabase.child("user").child("userName").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                result.onSucceed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.onError(e);
            }
        });


    }


    private void ReadUserData (CloudResult result) {

        firebaseDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                theObserver.setLimitedSize(userModel.getLimitedSize());
                theObserver.SaveSize(userModel.getSize());
                result.onSucceed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result.onError(error.toException());
            }
        });

    }

    public void AccountPremium (CloudResult cloudResult ) {
            firebaseDatabase.child("user").setValue(new UserModel(mAuthUser.getEmail(),mAuthUser.getUid(),mAuthUser.getDisplayName(),
                    Objects.requireNonNull(mAuthUser.getPhotoUrl()).getPath(),
                    true,theObserver.getSize(),21474836480L)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    cloudResult.onSucceed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cloudResult.onError(e);

                }
            });

    }


    private void StartRenameFile (String key,String NewName,CloudResult result) {
        firebaseDatabase.child("Files").child(key).child("name").setValue(NewName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
              result.onSucceed();

            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.onError(e);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void  saveDataRealTime (String extension ,Uri fileReference , Uri uri , String FileName , CloudResultProgress result ) {
       // boolean isMed = extension.contains("video") || extension.contains("image") || extension.contains("audio");
        FileModel fileModel = new FileModel(fileReference.toString(), FileName,
               System.currentTimeMillis(), getRealSizeFromUri(uri), extension);
        firebaseDatabase.child("Files").push().setValue(fileModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UserSizeUpdate(result);
                result.onSucceed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             result.onError(e);
            }
        });
        Log.e("firebase", "Error getting data", null);
    }

    private Long getRealSizeFromUri(Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.SIZE };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            cursor.moveToFirst();
            return cursor.getLong(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void UserSizeUpdate (CloudResultProgress progress ) {
          firebaseDatabase.child("user").child("size").setValue(theObserver.getSize()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progress.onSucceed();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.onError(e.getCause());
            }
        });
    }


    public interface CloudResult {
        void onError (Throwable throwable) ;
        void onSucceed();
    }

    public interface CloudResultProgress {
        void onError (Throwable throwable) ;
        void onSucceed();
        void onProgress(int progress);
        void StorageFirebase (StorageTask upload);
    }


}
