const express = require("express");
const fs = require('fs');
const path = require('path');
const app = express();

app.use(express.json({limit: '50mb'}));

app.use(express.static('public'));

app.post('/pb/upload', (req, res) => {
    // 获取 Base64 图片数据
    const base64Data = req.body.image;
    const matches = base64Data.match(/^data:([A-Za-z-+\/]+);base64,(.+)$/);
    const contentType = matches[1];
    const imageBuffer = Buffer.from(matches[2], 'base64');

    // 确定文件扩展名
    let fileExtension;
    switch (contentType) {
        case 'image/jpeg':
            fileExtension = 'jpg';
            break;
        case 'image/png':
            fileExtension = 'png';
            break;
        // 添加更多格式
        default:
            return res.status(400).send('Unsupported file type.');
    }

    // 生成基于时间戳的文件名
    const timestamp = Date.now();
    const uuid = timestamp.toString() + Math.random().toString();

    // 设置图片保存路径
    const imagePath = path.join(__dirname, 'public/images', `${uuid}.${fileExtension}`);

    // 保存图片
    fs.writeFile(imagePath, imageBuffer, (err) => {
        if (err) {
            return res.status(500).send('Error saving image.');
        }
        res.send({message: `images/${uuid}.${fileExtension}`});
    });
});

app.get('/pb/all', (req, res) => {
    fs.readdir('./public/images', (err, files) => {
        if (err) {
            return res.status(500).send('Error get images.');
        }
        files.sort(function (a, b) {
            return fs.statSync('./public/images/' + b).mtime.getTime() -
                fs.statSync('./public/images/' + a).mtime.getTime();
        });
        res.send({message: files.slice(0, 5)});
    });
});

module.exports = app;
