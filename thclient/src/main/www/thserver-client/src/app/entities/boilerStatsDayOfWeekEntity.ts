export class BoilerStatsDayOfWeekEntity {

  constructor(
    public dayOfWeekStartingMonday: number,
    public dayOfWeekText: string,
    public sumBoilerDiffIncrease: number,
    public sumBoilerDiffDecrease: number,
    public numOfStatisticRecords1: number,
  ) {
  }

  static emptyInstance() {
    return new BoilerStatsDayOfWeekEntity(0, '', 0, 0, 0);
  }

  static ofWebService(data: any): BoilerStatsDayOfWeekEntity {
    if (data == null) {
      return this.emptyInstance();
    } else {
      return new BoilerStatsDayOfWeekEntity(
        data.dayOfWeekStartingMonday,
        data.dayOfWeekText,
        data.sumBoilerDiffIncrease,
        data.sumBoilerDiffDecrease,
        data.numOfStatisticRecords1,
      );
    }
  }
}
