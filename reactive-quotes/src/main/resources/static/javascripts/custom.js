	var api = "http://localhost:9000/api/";

    var options = {
                  title: {
                      text: 'Java Processes CPU Load'
                  },
                  chart : {
                    renderTo : 'chart',
                    defaultSeriesType : 'spline'
                  },
                  yAxis: {
                      title: {
                          text: 'CPU Load'
                      }
                  },
                  legend: {
                      layout: 'vertical',
                      align: 'right',
                      verticalAlign: 'middle'
                  },
                  xAxis: {
                      type: 'datetime',
                  },
                  series: []

              };

    var chart = new Highcharts.Chart('chart', options );


	if (!!window.EventSource) {
		var eventSource = new EventSource(api + "cpu");

		eventSource.onmessage = function(e) {
			var datapoint = JSON.parse(e.data)
			console.log(datapoint);

            var serie = chart.series.find(serie => serie.name == datapoint.pid);
            if(serie) {
                var shift = serie.data.length > 30;
                serie.addPoint([datapoint.instant, datapoint.load], true, shift);
            }


			//var index = datapoint.id % chart.series.length;
			//chart.series[index].addPoint({
			//	x : datapoint.instant,
			//	y : datapoint.price
			//}, true, chart.series[index].data.length >= 50);

			// request new processes info if processes have changed
			if (datapoint.changed) {
				updateProcesses();
			}
		};

		eventSource.addEventListener('open', function(e) {
			console.log('Opened: ' +e);
			}, false);

		$('#content').bind('unload', function() {
			eventSource.close();
		});

	    updateProcesses();
	}

	function updateProcesses() {
		$.getJSON(api + "processes")
			.done(function(data) {
				var items = [];
				var series = [];
				data.forEach( info => {
					items.push("<li><span class='pid'>PID: " + info.pid
						+ "</span> - <span class='command'>" + info.command + "</span></li>");
					var existing = chart.series.find( serie => serie.name === info.pid);
					console.log(existing);
					var existingData = [];
					if(existing) {
					    existingData = existing.data.map( point => ({x: point.x, y: point.y}));
					}
//					console.log(existingData);
                    series.push( { name: info.pid, data: existingData } );
			    });
			    chart.update({series: series}, true, true);
				$("#processes").html(items.join(""));
		});
	}