#import <Foundation/Foundation.h>

@class A0Lock;
@interface com_codename1_auth0_LockNativeInterfaceImpl : NSObject {
    
}
@property (readonly, nonatomic) A0Lock *lock;

-(void)showLockScreen;
-(BOOL)isSupported;
+(com_codename1_auth0_LockNativeInterfaceImpl*)sharedInstance;
@end
