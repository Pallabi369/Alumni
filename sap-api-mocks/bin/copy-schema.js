const fs = require('fs-extra');

const srcDir = '../alumni-common/src/main/resources/json/';

function copySchema() {
    fs.copySync(srcDir, 'schema/')
    console.log('Schema files copied')
}

copySchema();
