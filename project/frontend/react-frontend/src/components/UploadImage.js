import React, { useState } from "react";
import axios from "axios";

function UploadImage() {
  const [file, setFile] = useState(null);
  const [zoom, setZoom] = useState(100); // Default zoom percentage is 100%
  const [imageData, setImageData] = useState(null); // Store the image data for further processing

  // Handle file change and preview the image on the canvas
  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);

    if (selectedFile) {
      const canvas = document.getElementById("myCanvas");
      const img = new window.SimpleImage(e.target);
      img.drawTo(canvas); // Display the image on the canvas

      setImageData(img);
    }
  };

  // Handle zoom percentage change
  const handleZoomChange = (e) => {
    const zoomValue = e.target.value;
    setZoom(zoomValue);

    if (imageData) {
      // Update image size according to zoom value
      const canvas = document.getElementById("myCanvas");
      const context = canvas.getContext("2d");

      context.clearRect(0, 0, canvas.width, canvas.height); 

      // Resize the canvas based on zoom
      const newWidth = (imageData.getWidth() * zoomValue) / 100;
      const newHeight = (imageData.getHeight() * zoomValue) / 100;
      canvas.width = newWidth;
      canvas.height = newHeight;

      // Draw the zoomed image on the canvas
      imageData.drawTo(canvas);
    }
  };

  // Submit the form to the backend
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      alert("Please upload an image");
      return;
    }

    // Prepare the form data to send
    const formData = new FormData();
    formData.append("image", file);
    formData.append("zoom", zoom);

    try {
      // Send data to the backend via REST API
      const response = await axios.post("http://localhost:8080/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      if (response.data) {
        alert("Image uploaded and processing started");

        // If you want to show the response data, you can update the state
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