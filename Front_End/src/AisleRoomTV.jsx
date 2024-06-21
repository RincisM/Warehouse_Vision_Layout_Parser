import React, { useRef, useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, PerspectiveCamera } from '@react-three/drei';
import EntireRoom from './EntireRoom';
import Aisle from './Aisle';


const AisleRoomTV = (props) => {

  const jsonData = props.data;  
  const { aisle, entireroom} = jsonData;
  const { length, width, height } = entireroom;
 
  const controls = useRef();
 
  useEffect(() => {
    //  console.log(aisle,robot);
  }, []);
 
  const handleZoom = () => {
     const camera = controls.current.object;
    //  console.log("Camera Position:", camera.position.toArray());
    //  console.log("Camera Rotation:", camera.rotation.toArray());
  };
 


return (
  <div  >
      <Canvas style={{ position: 'absolute' ,background: 'linear-gradient(to bottom, rgb(10, 10, 50) 0%,rgb(60, 10, 60) 100%)' }}>
        <ambientLight />
        <PerspectiveCamera
          makeDefault
          position={[0,1120,0]}
          rotation={[-1.5636345867838892, 0.0000031273628530415463, 0.0004366689074517221]}
          fov={70}
        />
        <OrbitControls
          enableDamping
          ref={controls}
          initialZoom={10000}
          maxPolarAngle={Math.PI / 2}
          enableZoom={true}
          maxDistance={2020}
          zoomToCursor={true}
          onChange={handleZoom}
          minDistance={-100}
        />

        <EntireRoom position={[0, 0, 0]} length={length} width={width} height={height} />
        {aisle.map((aisle, index) => (
          <Aisle
            key={index}
            aisleId={aisle.aisleId}
            bayCnt={aisle.bayCnt}
            position={aisle.position}
            rowCnt={aisle.rowCnt}
            rowwidth={aisle.rowwidth}
            rowLength={aisle.rowLength}
            rowHeight={aisle.rowHeight}
            tierCnt={aisle.tierCnt}
            aisleLength={aisle.aisleLength}
            aisleWidth={aisle.aisleWidth}
            splitByWidth={aisle.splitByWidth}
          />
        ))}
            
      </Canvas>
    </div>
);
};
 
export default AisleRoomTV;

