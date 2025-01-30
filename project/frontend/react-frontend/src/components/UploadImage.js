import React, { useState, useEffect } from "react";
import axios from "axios";

function UploadImage() {
  const [file, setFile] = useState(null);
  const [zoom, setZoom] = useState(100); 
  const [imageSrc, setImageSrc] = useState(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);

    if (selectedFile) {
      const reader = new FileReader();

      reader.onload = (event) => {
        setImageSrc(event.target.result); 
      };

      reader.readAsDataURL(selectedFile);
    }
  };

  useEffect(() => {
    if (imageSrc) {
      const img = new Image();
      img.src = imageSrc;

      img.onload = () => {
        const canvas = document.getElementById("myCanvas");
        const ctx = canvas.getContext("2d");

        const newWidth = (img.width * zoom) / 100;
        const newHeight = (img.height * zoom) / 100;
        
        canvas.width = newWidth;
        canvas.height = newHeight;
        
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(img, 0, 0, newWidth, newHeight);
      };
    }
  }, [imageSrc, zoom]);

  const handleZoomChange = (e) => {
    setZoom(Number(e.target.value));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      alert("Please upload an image");
      return;
    }

    const formData = new FormData();
    formData.append("image", file);
    formData.append("zoom", zoom);

    try {
      const response = await axios.post(
        process.env.REACT_APP_BACKEND_URL || "http://localhost:8080/upload",
        formData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      if (response.data) {
        alert("Image uploaded and processing started");
        console.log("Server response:", response.data);
      }
    } catch (error) {
      console.error("Error uploading image:", error);
    }
  };

  return (
    <div className="container">
      <h1>Image Magnifier App</h1>
      <form onSubmit={handleSubmit}>
        <p>
          <input
            type="file"
            accept="image/bmp"
            onChange={handleFileChange}
            multiple={false}
            id="finput"
          />
          <button type="submit" id="uploadButton">Upload Image</button>
        </p>
        <canvas width="400" height="400" id="myCanvas"></canvas>
        <p>
          <label htmlFor="zoomInput" className="fontClass">Choose Zoom Percentage</label>
          <input
            type="number"
            value={zoom}
            onChange={handleZoomChange}
            min="50"
            max="200"
            id="zoomInput"
          />
        </p>
      </form>
    </div>
  );
}

export default UploadImage;
