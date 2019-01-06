import * as React from "react";
import * as ReactDom from "react-dom";
import {LineChart, Line, XAxis, Tooltip, CartesianGrid, YAxis} from "recharts";

const BUFFER_SIZE = 100;

interface DataPackage {
    source: String;
    value: Number;
    timestamp: Number;
}

interface SimulatorState {
    data: Array<DataPackage | null>;
}

export class Simulator extends React.Component<{}, SimulatorState> {

    constructor(props: {}, context?: any) {
        super(props, context);
        const data: Array<DataPackage | null> = [];
        for (let i = 0; i < BUFFER_SIZE; i++) {
            data[i] = null;
        }
        this.state = {data};
    }

    render() {
        return (
            <div className="Simulator">
                <LineChart data={this.state.data} width={500} height={300}>
                    <XAxis dataKey="timestamp" />
                    <YAxis />
                    <Tooltip />
                    <CartesianGrid stroke="#f5f5f5" />
                    <Line type="monotone" dataKey="value" stroke="#ff7300" yAxisId={0} />
                </LineChart>
            </div>
        );
    }

    componentDidMount() {
        fetch("/api/1.0/data/Whatever", {method: "POST"})
            .then(response => response.body)
            .then(async body => {
                const reader = body.getReader();
                let result;
                while (!result || !result.done) {
                    result = await reader.read();
                    const newPackage = this.binArrayToObject(result.value);
                    this.roundingInsert(newPackage);
                }
            });
    };

    private binArrayToObject(binArray: Uint8Array): DataPackage {
        const json = Array.from(binArray)
            .map(intVal => String.fromCharCode(intVal))
            .join("")
        return JSON.parse(json) as DataPackage
    }

    private roundingInsert(value: DataPackage) {
        this.setState((prevState) => {
            const data: Array<DataPackage | null> = [];
            for (let i = 0; i < BUFFER_SIZE; i++) {
                data[i + 1] = prevState.data[i];
            }
            data[0] = value;
            return {data};
        });
    }
}