# auth0-codenameone
Library extension of the Auth0 SDK (https://auth0.com/) for the Codename One platform.(http://www.codenameone.com)

This plugin supports the Lock screen login - https://auth0.com/lock

The library is implemented for Android and iOS.

##Integration

1. Build the project <br/>
2. Place the CN1Auth0Lib.cn1lib file in your CN1 project lib. <br/>
3. Right click on your CN1 project and select "Refresh Libs" then clean build your project.
4. Add the build hints `var.auth0.clientId` and `var.auth0.domain` to your project's build hints.

## Sample of Usage

```java
        Form hi = new Form("Auth0 Demo");
        hi.setLayout(new BorderLayout());
        
        hi.add(BorderLayout.SOUTH, new Button(Command.create("Sign In", null, e -> {
            
            if(LockManager.getInstance().isSupported()){
                LockManager.getInstance().showLockScreen(new LoginCallback() {

                    @Override
                    public void loggedIn(String token, UserProfile p) {
                        System.out.println("LoggedIn!!!!!!!!!!");
                        System.out.println("token " + token);
                        System.out.println("name " + p.getName());
                        
                    }
                });
            }
            
        })));
        hi.show();
```

