Add it in your root build.gradle at the end of repositories:
    
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
Add the dependency
	dependencies {
	        implementation 'com.github.InochiSoft:HighlighterView:1.0'
	}

Sample usage:
in layout:


    <com.inochi.highlighter.CodeView
        android:id="@+id/codeView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/grey_500"
        app:language="php"
        app:text_size="16sp"
        app:number_background="@color/red_900"
        app:number_color="@color/yellow_200"
        app:text="&lt;?php echo 'Test' ?&gt;"
     />
    
    
in source code (Activity):

        String code = "class MainApp extends StatelessWidget {\n" +
                "  @override\n" +
                "  Widget build(BuildContext context) {\n" +
                "    return MaterialApp(\n" +
                "      title: 'Buku Tamu',\n" +
                "      theme: ThemeData(\n" +
                "        primarySwatch: Colors.brown,\n" +
                "      ),\n" +
                "      home: MainActivity(title: 'Buku Tamu'),\n" +
                "    );\n" +
                "  }\n" +
                "}";
        
        CodeView codeView = findViewById(R.id.codeView);
        codeView.setLanguage(Language.DART);
        codeView.setText(code);
        