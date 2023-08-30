
const fs = require('fs')

if (!fs.existsSync('schema')) {
  require('../../bin/copy-schema.js')
}

module.exports = [
  {
    id: "clone",
    url: "/clone",
    method: "GET",
    variants: [
      {
        id: "success",
        response: (req, res, next, mocksServer) => {
          if (req.query.user && /^([0-9]{3}-[0-9]{8})$/.test(req.query.user)) {

            let path = ['mocks/cloned-users/', req.query.user, '.personal.json'].join('');
            if (!fs.existsSync(path)) {
              path = ['mocks/cloned-users/099-20000001.personal.json'].join('');
            }

            const userData = JSON.parse(fs.readFileSync(path, 'utf8'));
            Object.keys(userData).map(infotype => {
              for (const it of userData[infotype]) {
                if (it.PERNR) {
                  it.PERNR = req.query.user.split('-')[1];
                }
              }
            })
            res.status(200).send(userData);
          } else {
            res.status(404).send("User not provided: /clone?user={zalarisId}");
          }
        },
      }
    ]
  },
  {
    id: "payroll",
    url: "/payroll-results",
    method: "GET",
    variants: [
      {
        id: "success",
        response: (req, res, next, mocksServer) => {
          if (req.query.user && /^([0-9]{3}-[0-9]{8})$/.test(req.query.user)) {

            let path = ['mocks/cloned-users/', req.query.user, '.payroll.json'].join('');
            if (!fs.existsSync(path)) {
              path = ['mocks/cloned-users/099-20000001.payroll.json'].join('');
            }

            const payrollData = JSON.parse(fs.readFileSync(path, 'utf8'));
            payrollData['payrollResults'].map(r => {
              r.pernr = req.query.user.split('-')[1];
            })

            res.status(200).send(payrollData);
          } else {
            res.status(404).send("User not provided: /payroll-results?user={zalarisId}");
          }
        }
      }
    ]
  },
  {
    id: "payslip",
    url: "/payslip",
    method: "GET",
    variants: [
      {
        id: "success",
        response: (req, res, next, mocksServer) => {
          if (req.query.user && req.query.id) {
            const path = ['mocks/cloned-users/sample.payslip.pdf'].join('');
            res.contentType("application/octet-stream");
            res.status(200).send(fs.readFileSync(path, 'utf8'));
          } else {
            res.status(404).send("User or id not provided: /payslip?user={zalarisId}&id={fileIdentifier}");
          }
        }
      }
    ]
  }
];


