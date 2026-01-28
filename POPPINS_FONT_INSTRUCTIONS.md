# How to Add Poppins Font to Your App

## Yes, you need to download Poppins font files and add them to the res/font/ folder!

### Step-by-Step Instructions:

1. **Download Poppins Font:**
   - Visit: https://fonts.google.com/specimen/Poppins
   - Click "Download family" button
   - Extract the ZIP file

2. **Get the Required Font Files:**
   From the extracted folder, you need these 4 files:
   - `Poppins-Regular.ttf`
   - `Poppins-Medium.ttf`
   - `Poppins-SemiBold.ttf`
   - `Poppins-Bold.ttf`

3. **Add to Your Project:**
   - Navigate to: `app/src/main/res/font/` folder
   - If the `font` folder doesn't exist, create it
   - Copy and rename the files:
     - `Poppins-Regular.ttf` → rename to `poppins_regular.ttf`
     - `Poppins-Medium.ttf` → rename to `poppins_medium.ttf`
     - `Poppins-SemiBold.ttf` → rename to `poppins_semibold.ttf`
     - `Poppins-Bold.ttf` → rename to `poppins_bold.ttf`

4. **Recreate the Font Family File:**
   - Create `app/src/main/res/font/poppins_family.xml` with this content:

```xml
<?xml version="1.0" encoding="utf-8"?>
<font-family xmlns:android="http://schemas.android.com/apk/res/android">
    <font
        android:fontStyle="normal"
        android:fontWeight="400"
        android:font="@font/poppins_regular" />
    <font
        android:fontStyle="normal"
        android:fontWeight="500"
        android:font="@font/poppins_medium" />
    <font
        android:fontStyle="normal"
        android:fontWeight="600"
        android:font="@font/poppins_semibold" />
    <font
        android:fontStyle="normal"
        android:fontWeight="700"
        android:font="@font/poppins_bold" />
</font-family>
```

5. **Update All Layouts:**
   - Replace `android:fontFamily="sans-serif"` with `android:fontFamily="@font/poppins_family"` in all layout files
   - Or I can do this for you once you add the font files!

6. **Rebuild the Project:**
   - Clean and rebuild
   - Poppins font will now be used throughout the app!

## Current Status:
Right now, the app uses `sans-serif` which looks very similar to Poppins. Once you add the Poppins font files, the app will automatically use the actual Poppins font.

