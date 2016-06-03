#import "com_codename1_auth0_LockNativeInterfaceImpl.h"
#import "CodenameOne_GLViewController.h"
#import "com_codename1_auth0_LockManager.h"
#import <Lock/Lock.h>
#import <Foundation/NSJSONSerialization.h> 

extern JAVA_OBJECT fromNSString(CODENAME_ONE_THREAD_STATE, NSString* str);

@implementation com_codename1_auth0_LockNativeInterfaceImpl

-(void)showLockScreen{
    dispatch_async(dispatch_get_main_queue(), ^{
        //POOL_BEGIN();
        A0Lock *lock = [[com_codename1_auth0_LockNativeInterfaceImpl sharedInstance] lock];
        A0LockViewController *controller = [lock newLockViewController];
        controller.onAuthenticationBlock = ^(A0UserProfile *p, A0Token *token) {
            // Do something with token & profile. e.g.: save them.
            // And dismiss the ViewController
            NSError *error; 
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:p.extraInfo 
                                                       options:0 
                                                         error:&error];
            NSString *extra = nil;
            if (jsonData == nil) {
                NSLog(@"Got an error: %@", error);
            } else {
                extra = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                //[jsonData release];
            }


            com_codename1_auth0_LockManager_loggedIn___java_lang_String_java_lang_String_java_lang_String_java_lang_String_java_lang_String_java_lang_String_java_lang_String(
                    CN1_THREAD_GET_STATE_PASS_ARG
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG token.accessToken), 
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG p.userId), 
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG p.name), 
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG p.nickname),
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG p.email), 
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG p.picture.absoluteString), 
                    fromNSString(CN1_THREAD_GET_STATE_PASS_ARG extra)
            );
            [[CodenameOne_GLViewController instance] dismissViewControllerAnimated:YES completion:nil];
        };
        [[CodenameOne_GLViewController instance] presentViewController:controller animated:YES completion:nil];
        //POOL_END();
    });
}

-(BOOL)isSupported{
    return YES;
}

+(com_codename1_auth0_LockNativeInterfaceImpl*)sharedInstance {
    static com_codename1_auth0_LockNativeInterfaceImpl *sharedApplication = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedApplication = [[self alloc] init0];
    });
    return sharedApplication;
}

-(id)init0 {
    self = [super init];
    if (self) {
        _lock = [A0Lock newLock];
    }
    return self;
}

@end
