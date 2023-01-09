import React, {Component} from "react";

// Props of the Information Component
// ↪ start   : string that stores the start of the path
// ↪ end     : string that stores the end of the path
// ↪ distance: number that stores the distance of the whole path
// ↪ isHidden: boolean that determines if the Component is shown
// ↪ speed   : number that stores the speed of the current way of transport
interface InformationProps {
    start: string,
    end: string,
    distance: number,
    isHidden: boolean,
    speed: number,
}

// This class is used to display information about the map to the user
class Information extends Component<InformationProps, {}> {

    render(){
        let time: number = Math.round(Math.round(this.props.distance/this.props.speed)/60);
        // this if statement determines if the component is shown
        if(!this.props.isHidden) {
            return(
                <div>
                    <p id="info-paragraph">
                        The distance from {this.props.start} to {this.props.end} is {this.props.distance} feet</p>
                    <p id ="info-paragraph"> It will take you {time} minute(s)</p>
                </div>
            )
        } else {
            return(<p id="info-paragraph">Select two buildings bellow and click draw to get a path</p>)
        }
    }
}

export default Information;