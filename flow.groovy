node {
    stage('Checkout'){
        dir('sell-wut-js-client') {
            git credentialsId: 'e97c534a-b9df-40d5-981f-52de748368f4', url: 'https://github.com/tendag/sell-wut-js-client.git'
        }
        
        dir('sell-admin') {
            git credentialsId: 'e97c534a-b9df-40d5-981f-52de748368f4', url: 'https://github.com/tendag/sell-admin.git'
        }
    }
    
    stage('Build'){
        env.PATH=env.PATH+"/sbin:/usr/sbin:/bin:/usr/bin:/usr/local/bin"
        echo env.PATH
        env.PHANTOMJS_BIN="/usr/bin/phantomjs"
        env.CHROME_BIN="/usr/bin/google-chrome"
        sh "Xvfb :1 -screen 0 1600x1200x16 &"
        env.DISPLAY=":1.0"
        //dir('sell-admin') {
        dir('sell-wut-js-client') {
            sh "npm install"    
        }
    }
    
    stage('Test'){
        //dir('sell-admin') {
        dir('sell-wut-js-client') {
            //sh "node node_modules/karma/bin/karma start test/karma.conf.js --single-run --browsers=Chrome #--stacktrace --browser-disconnect-timeout=12000"  
            try{
            sh "./node_modules/karma/bin/karma start karma.conf.js --single-run --browsers Chrome"
            } catch(err){
                echo "Test fail"   
            }
        }
        
    }
    
    stage('Report'){
        step([$class: 'JUnitResultArchiver', testResults: 'sell-wut-js-client/test/results/test-results.xml'])
        //junit 'sell-admin/test/results/test-results.xml'
        echo "Done report"
    }
}
