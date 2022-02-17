export class BoilerStatsByHourEntity {

  constructor(
    public hourOfTheDay: number,
    public sumBoilerDiffIncrease: number,
    public sumBoilerDiffDecrease: number,
    public numOfStatisticRecords1: number,
  ) {
  }

  static emptyInstance() {
    return new BoilerStatsByHourEntity(0, 0, 0, 0);
  }

  static ofWebService(data: any):BoilerStatsByHourEntity {
    if (data == null) {
      return this.emptyInstance();
    } else {
      return new BoilerStatsByHourEntity(
        data.hourOfTheDay,
        data.sumBoilerDiffIncrease,
        data.sumBoilerDiffDecrease,
        data.numOfStatisticRecords1,
      );
    }
  }
}
