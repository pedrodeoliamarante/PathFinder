
import {LatLngExpression} from "leaflet";
import React, {Component} from "react";
import {MapContainer, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import {
    UW_LATITUDE_CENTER,
    UW_LONGITUDE_CENTER,
} from "./Constants";
import {Edge, PathSegment} from "./types";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

// Props of the map
// ↪ paths: A list of PathSegment objects that are to be drawn in the map
interface MapProps {
  paths: PathSegment[];
}

// This component represents a map and its lines in the App
class Map extends Component<MapProps, {}> {

  render() {

    // iterates through the path prop and adds the paths presents in it into an array
    let colorList: string[] = ["red", "orange", "yellow", "green", "blue", "indigo", "violet"];
    let edgeList: Edge[] = [];
    for (let i = 0; i < this.props.paths.length; i++) {
        let tempEdge: Edge = {
            x1: this.props.paths[i].start.x,
            x2: this.props.paths[i].end.x,
            y1: this.props.paths[i].start.y,
            y2: this.props.paths[i].end.y,
        };
        edgeList.push(tempEdge);
    }

    // uses the edgeList array to create an array of JSX elements that are lines to be placed on the map
    let arr: JSX.Element[] = [];
    for (let i = 0; i < edgeList.length; i++) {
        let x1: number = edgeList[i].x1;
        let y1: number = edgeList[i].y1;
        let x2: number = edgeList[i].x2;
        let y2: number = edgeList[i].y2;
        let lineElement: JSX.Element =
                <MapLine x1={x1} y1={y1} x2={x2} y2={y2} key={"" + i} color={colorList[i%7]}/>;
        arr.push(lineElement);

        }

    return (

      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {
              <div>
                  {arr}
              </div>
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
