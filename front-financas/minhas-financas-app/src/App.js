import React from 'react';

class App extends React.Component {

  state = {
    numero1: '',
    numero2: '',
    resultado: ''
  }

  somar = () =>{
    const resultado =  parseInt(this.state.numero1) + parseInt(this.state.numero2)
    this.setState({ resultado: resultado})

  }

  render() {
    return (
      <div>
        <label>Número 1:</label>
        <input type="text" value={this.state.nome} onChange={(e) => this.setState({ numero1: e.target.value })} />
        <br />
        <label>Número 2:</label>
        <input type="text" value={this.state.nome} onChange={(e) => this.setState({ numero2: e.target.value })} />
        <br />
        <button onClick={this.somar}>Somar</button>
        <br />
        A soma de numero1 com numero2: {this.state.resultado}
      </div>
    )
  }
}

export default App;
