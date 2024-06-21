import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AisleRoomTV from './AisleRoomTV'
import ImageUploader from './ImageUpload';
import JsonDisplay from './JsonDisplay';
import './styles.css'

const convertAisleData = (inputJson) => {
  console.log(JSON.stringify(inputJson))
  const originalData = inputJson.aisles;
  const entireroom = inputJson.entireroom
  const height = entireroom.height;
  const width = entireroom.width;

  const newData = {
      entireroom: {
          length: width + 400,
          width: height + 200,
          height:200
      },    
      aisle: []
  };

  originalData.forEach((aisle, index) => {
      const normalizedX = aisle.x - width / 2;  
      const normalizedZ = aisle.y - height / 2;

      const splitByWidth = aisle.width > aisle.height;

      const rowwidth = splitByWidth ? aisle.width / aisle.rack_count : aisle.height / aisle.rack_count;
      const rowLength = splitByWidth ? aisle.height : aisle.width;

      const newAisle = {
          aisleId: aisle.Aisle_id,
          bayCnt: 6,
          position: [
              normalizedX,
              0,
              normalizedZ
          ],
          rowCnt: aisle.rack_count,
          aisleWidth: aisle.width,
          aisleLength: aisle.height,
          rowwidth: rowLength,
          rowLength: rowwidth,

          rowHeight: 200,
          tierCnt: 5,
          splitByWidth: splitByWidth
      };

      newData.aisle.push(newAisle);
  });

  return newData;
};


function App() {

  const [uploadData, setUploadData] = useState('');
  const [originalData, setOriginalData] = useState('');

  const handleUploadComplete = (data) => {
    setOriginalData(data);
    setUploadData(convertAisleData(data));
  };

  return (
    <Router>
      <div className="app-container">
        <div className="banner-container"></div> {/* Banner image container */}
        <div className="project-info">
          <h1>Warehouse Vision Layout Parser</h1>
          <p><b>Rincis Melvin M</b></p>
          <p>2022179025 MCA (SS)</p>
          <p><b>External Guide:</b> Mr. Timothy Alex (Senior Project Lead, Roboteon)</p>
          <p><b>Internal Guide:</b> Dr. D. Narashiman (Teaching Fellow, IST)</p>
          <p id='abstract'>In response to the imperative for efficient warehouse management systems, this project proposes an innovative solution that merges software development with 
            advanced computer vision techniques. Leveraging Spring Boot as the back-end framework, the project aims to automate the extraction of crucial information from 
            Computer-Aided Design and Drafting (CADD) drawings of warehouse layouts. By employing techniques like U-Shaped Network (U-Net) for initial segmentation and the contour detection, 
            the application facilitates the identification and of aisles and racks within warehouse layouts. This integration offers a streamlined approach to extract and 
            interpret data from CADD/Warehouse floor plan drawings, laying the groundwork for comprehensive warehouse management systems that optimize productivity and accuracy 
            in logistics operations</p>
        </div>
        <Routes>
          <Route path="/" element={
            <>
              <div className="uploader-container">
                <ImageUploader onUploadComplete={handleUploadComplete} />
              </div>
              <div className="json-display-container">
                {uploadData && <JsonDisplay data={originalData} />} {/* Display JSON data */}
              </div>
              <div className="canvas-container">
                {uploadData && <AisleRoomTV data={uploadData} />}
              </div>
            </>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;