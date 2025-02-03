const express = require("express");
const mongoose = require("mongoose");
const fs = require("fs");
const mysql = require('mysql2');
require('dotenv').config();

const app = express();
const PORT = 3000;

const connection = mysql.createConnection({
  host: process.env.DB_HOST || '127.0.0.1',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || 'password',
  database: process.env.DB_NAME || 'zoomed_images',
  port: 3306
});

connection.connect(err => {
    if (err) {
        console.error("MySQL Connection Failed:", err);
    } else {
        console.log("Connected to MySQL!");
    }
});

// Connect to MongoDB (for SNMP data)
mongoose
  .connect("mongodb://localhost:27017/mydatabase")
  .then(() => console.log("Connected to MongoDB"))
  .catch((err) => console.error("MongoDB Connection Failed:", err));

// REST API to fetch an image
app.get("/image/:filename", (req, res) => {
  const filename = req.params.filename;

  connection.query("SELECT data FROM images WHERE filename = ?", [filename], (err, results) => {
    if (err) {
      console.error("⛔ Error fetching image:", err);
      return res.status(500).send("Error fetching image");
    } else if (results.length === 0) {
      return res.status(404).send("Image not found");
    } else {
      res.writeHead(200, { "Content-Type": "image/bmp" });
      res.end(results[0].data);
    }
  });
});

// REST API to fetch SNMP data
app.get("/snmp", async (req, res) => {
  try {
    const SNMPModel = mongoose.model("SNMPData", new mongoose.Schema({}, { strict: false }));
    const snmpData = await SNMPModel.find();
    res.json(snmpData);
  } catch (err) {
    console.error("⛔ Error fetching SNMP data:", err);
    res.status(500).json({ error: "Error fetching SNMP data" });
  }
});


app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});

