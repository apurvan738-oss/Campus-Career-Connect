// Runner script to start Spring Boot Student Placement Management System on port 3000
const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

const jarPath = path.resolve(__dirname, 'target', 'student-placement-management-system-1.0.0.jar');

if (!fs.existsSync(jarPath)) {
    console.log('JAR not found at ' + jarPath + ', running maven package first...');
    const build = spawn('mvn', ['package', '-DskipTests'], { stdio: 'inherit' });
    build.on('close', (code) => {
        if (code === 0) {
            startJar();
        } else {
            console.error('Maven build failed with exit code ' + code);
            process.exit(code);
        }
    });
} else {
    startJar();
}

function startJar() {
    console.log('Starting Spring Boot application on port 3000...');
    const child = spawn('java', [
        '-jar',
        jarPath,
        '--server.port=3000',
        '--spring.profiles.active=dev'
    ], {
        stdio: 'inherit',
        env: process.env
    });

    child.on('error', (err) => {
        console.error('Failed to start Java process:', err);
    });

    child.on('close', (code) => {
        console.log('Spring Boot application exited with code ' + code);
        process.exit(code);
    });

    process.on('SIGTERM', () => child.kill('SIGTERM'));
    process.on('SIGINT', () => child.kill('SIGINT'));
}
