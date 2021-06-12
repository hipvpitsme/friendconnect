'use strict';
//imports
let dns = require('native-dns');
let server = dns.createServer();
let async = require('async');
//event handlers
server.on('listening', () => console.log('server listening on', server.address()));
server.on('close', () => console.log('server closed', server.address()));
server.on('error', (err, buff, req, res) => console.error(err.stack));
server.on('socketError', (err, socket) => console.error(err));
//backup dns
let authority = { address: '1.1.1.1', port: 53, type: 'udp' };
//proxy function
function proxy(question, response, cb) {
  var request = dns.Request({
    question: question,
    server: authority,
    timeout: 1000
  });
  request.on('message', (err, msg) => {
    msg.answer.forEach(a => response.answer.push(a));
  });
  request.on('end', cb);
  request.send();
}
//entries
let entries = [
	{
      domain: "mco.lbsg.net",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    },
	{
      domain: "play.galaxite.net",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    },
	{
      domain: "play.pixelparadise.gg",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    },
	{
      domain: "mco.cubecraft.net",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    },
	{
      domain: "geo.hivebedrock.network",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    },
	{
      domain: "play.inpvp.net",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    },
	{
      domain: "mco.mineplex.com",
      records: [
        { type: "A", address: "108.178.12.134", ttl: 4 }
      ]
    }
 ];
//handle requests function
  function handleRequest(request, response) {
    console.log('request from', request.address.address, 'for', request.question[0].name);
    let f = [];
    request.question.forEach(question => {
      let entry = entries.filter(r => new RegExp(r.domain, 'i').exec(question.name));
      if (entry.length) {
        entry[0].records.forEach(record => {
            record.name = question.name;
            record.ttl = record.ttl || 1800;
            if (record.type == 'CNAME') {
              record.data = record.address;
              f.push(cb => proxy({ name: record.data, type: dns.consts.NAME_TO_QTYPE.A, class: 1 }, response, cb));
            }
            response.answer.push(dns[record.type](record));
          });
      } else {
        f.push(cb => proxy(question, response, cb));
      }
    });
    async.parallel(f, function() { response.send(); });
  }
//handle requests
server.on('request', handleRequest);
//start server
server.serve(53);