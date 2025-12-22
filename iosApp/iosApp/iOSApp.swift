import SwiftUI
import FirebaseCore
import ComposeApp
import RevenueCat
// veya projenin modül adına göre: import Shared

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        Purchases.logLevel = .debug
        Purchases.configure(withAPIKey: "test_pKLTyhZLvSGJmGDvRvoIvenCOfA")
        return true
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        KoinHelperKt.doInitKoin()
        }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
    
}

