import React, { useState,useEffect } from 'react';
import axios from 'axios';

function ImageUploader({ onUploadComplete }) {
  const [file, setFile] = useState(null);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (file) {
      const formData = new FormData();
      formData.append("file", file);

      try {
        const response = await axios.post("http://localhost:8080/collegeproject/process", formData);
        onUploadComplete(response.data);
      } catch (error) {
        console.error('Error uploading file:', error);
        onUploadComplete({ error: "Failed to upload file." });
      }
    }
  };

  // Listen for changes in the file state to trigger the upload
 useEffect(() => {
    if (file) {
      handleUpload();
    }
  }, [file]);

  return (
    <div>
      <button className='upload-button' onClick={() => document.getElementById('file-input').click()}>
        Upload Image
      </button>
      <input id="file-input" type='file'  onChange={handleFileChange} style={{ display: 'none'}}/>
    </div>
  );
}

export default ImageUploader;
