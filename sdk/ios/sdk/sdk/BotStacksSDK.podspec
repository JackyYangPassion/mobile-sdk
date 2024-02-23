Pod::Spec.new do |spec|
    spec.name                     = 'BotStacksSDK'
    spec.version                  = '0.0.1'
    spec.homepage                 = 'https://botstacks.ai'
    spec.source                      = { :git => "https://github.com/BotStacks/iOS-podspecs.git", :tag => spec.version }
    spec.authors                  = 'BotStacks'
    spec.license                  = 'MIT'
    spec.summary                  = 'BotStacks iOS SDK'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '15.0'
    spec.source_files = "Classes/**/*.swift"
    spec.static_framework = true

    spec.subspec 'botstacks_core' do |core|

      core.vendored_frameworks      = '../../pod/debug/botstacks_core.xcframework'
      core.resources                = ["../../pod/build/compose/ios/botstacks-core/compose-resources"]
      core.dependency 'Giphy', '2.2.8'
      core.dependency 'GoogleMaps', '8.4.0'
      core.dependency 'Sentry', '8.20.0'
    end
end