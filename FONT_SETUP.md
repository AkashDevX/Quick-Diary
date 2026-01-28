# Poppins Font Setup Instructions

To use Poppins font in the app, you need to add the Poppins font files to the project.

## Steps:

1. **Download Poppins Font Files:**
   - Go to Google Fonts: https://fonts.google.com/specimen/Poppins
   - Download the Poppins font family
   - Extract the following font files:
     - `Poppins-Regular.ttf` (400 weight)
     - `Poppins-Medium.ttf` (500 weight)
     - `Poppins-SemiBold.ttf` (600 weight)
     - `Poppins-Bold.ttf` (700 weight)

2. **Add Font Files to Project:**
   - Create the following directory if it doesn't exist: `app/src/main/res/font/`
   - Copy the font files and rename them:
     - `Poppins-Regular.ttf` → `poppins_regular.ttf`
     - `Poppins-Medium.ttf` → `poppins_medium.ttf`
     - `Poppins-SemiBold.ttf` → `poppins_semibold.ttf`
     - `Poppins-Bold.ttf` → `poppins_bold.ttf`
   - Place all 4 files in `app/src/main/res/font/` directory

3. **Rebuild the Project:**
   - Clean and rebuild the project
   - The app will now use Poppins font throughout

## Note:
If the font files are not added, the app will fall back to the system default sans-serif font, which is similar to Poppins.

