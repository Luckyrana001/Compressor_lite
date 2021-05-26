package id.zelory.compressor.sample.permissions;

import java.util.ArrayList;

public interface PermissionResultCallback {
    void NeverAskAgain(int i);

    void PartialPermissionGranted(int i, ArrayList<String> arrayList);

    void PermissionDenied(int i);

    void PermissionGranted(int i);
}
