let distinctionsAll = [];

fetch('api/destination')
    .then(res => res.json())
    .then((destinations) => {
        destinations.forEach(destination => {
            distinctionsAll.push({
                    id: destination.id,
                    airportCode: destination.airportCode,
                    airportName: destination.airportName,
                    cityName: destination.cityName,
                    timezone: destination.timezone,
                    countryName: destination.countryName
                }
            )
            $("#distinctionFrom").append($("<option value='"+distinctionsAll.length+"'>"
                                            +destination.cityName+"</option>"));
            $("#distinctionTo").append($("<option value='"+distinctionsAll.length+"'>"
                +destination.cityName+"</option>"));
        })
    });

async function createSearch() {
    function getDistinction(distinctionById) {
        return distinctionsAll[distinctionById-1];
    }

    let distinctionFrom = getDistinction(document.getElementById('distinctionFrom').value);
    let distinctionTo = getDistinction(document.getElementById('distinctionTo').value);

    fetch('api/search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            from: distinctionFrom,
            to: distinctionTo,
            departureDate: document.getElementById('departureDate').value,
            returnDate: document.getElementById('returnDate').value,
            numberOfPassengers: document.getElementById('numberOfPassengers').value,
        })
    })
        .then(() =>  {
            document.forms['search'].reset();

        })
}